package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import com.alibaba.fastjson.JSON;
import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.message.MessageHandleContext;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.HeartPackageHandle;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.ITagDecoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag0Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag10Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag11Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag1Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag20Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag251Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag252Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag253Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag.Tag30Decoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.DeviceTagReadVo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag1Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.service.DeviceCodeService;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PersonPositionResponseHandleContext extends SimpleChannelInboundHandler<PersonPositionMessage> {
	protected final static Logger logger = LogManager.getLogger(PersonPositionResponseHandleContext.class);
	private MessageHandleContext<PersonPositionMessage, Object> messageHandleContent;
	
	private PersonPositionEventBusService personPositionEventBusService;
	private DeviceCodeService deviceCodeService; // 解析设备回复的指令
	private IPersonPositionDeviceService personPositionDeviceService; // 人员定位相关服务
	
	private static int linkDeviceCount = 0; // 设备总数量
	private int deviceIndex; // 设备索引号
	
	public PersonPositionResponseHandleContext(PersonPositionEventBusService personPositionEventBusService, DeviceCodeService deviceCodeService, IPersonPositionDeviceService personPositionDeviceService) {
		super();
		this.personPositionEventBusService = personPositionEventBusService;
		this.deviceCodeService = deviceCodeService;
		this.personPositionDeviceService = personPositionDeviceService;
		this.messageHandleContent = new MessageHandleContext<>();
//		this.messageHandleContent.setOnlyHandle(false);
//		
//		this.messageHandleContent.addHandleClass(new LabelReadHandle(PersonPositionEventBusService)); // 读取下位机上传的标签信息的类
//		this.messageHandleContent.addHandleClass(new UScanChangeUploadULocationMegHandle(PersonPositionEventBusService)); // 处理下位机在U位信息有变化时主动上传U位扫描信息的类
		this.messageHandleContent.addHandleClass(new HeartPackageHandle()); // 下位机发送心跳包
//		this.messageHandleContent.addHandleClass(new TemperatureHandle(PersonPositionEventBusService)); // 温度处理
//		this.messageHandleContent.addHandleClass(new OnlineHandle());
//		this.messageHandleContent.addHandleClass(new OfflineHandle());
	}

	@NotProguard
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.deviceIndex = ++linkDeviceCount;
		// 电脑向设备发送读取阅读器配置参数指令
		byte[] datas = {0x02,0x03,0x04,0x05,0x00,0x0B,0x00,0x58,0x44,0x00,(byte) 0xB5};
		ByteBuf bs = Unpooled.copiedBuffer(datas);
		ChannelFuture cf = ctx.writeAndFlush(bs);
		// 回调函数监听是否发送成功
		cf.addListener(new ChannelFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					logger.info("发送读取阅读器配置参数命令成功,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
				} else {
					logger.error("发送读取阅读器配置参数命令失败,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
				}
			}
		});

		try {
			PersonPositionMessage msg = new PersonPositionMessage();
			msg.setCommand(Integer.valueOf(100).byteValue());
			this.messageHandleContent.handle(ctx, msg);
		} catch (Exception e) {
			logger.error("", e);
		}

//		MDC.put("ip", ip);
//		logger.info("RU2000设备已连接{}", ip);
//		MDC.remove("ip");
//		NetMapping.getInstance().addChannel(ctx.channel());
//		NetMapping.getInstance().addChannelMapping(ip, ctx.channel());
//		try {
//			ru2000Service.online(ip, ""); // 记录设备上线状态
//		} catch (Exception e) {
//			logger.error(e);
//		}
		super.channelActive(ctx);
	}

	@NotProguard
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		linkDeviceCount--;
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();

		PersonPositionMessage msg = new PersonPositionMessage();
		msg.setCommand(Integer.valueOf(-99).byteValue());
		try {
			this.messageHandleContent.handle(ctx, msg);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
//			if (PersonPositionWebSocket.getOnlineCount() > 0) {
//				PersonPositionMessageVo vo = PersonPositionMessageVoFactory.create(msg);
//				vo.setIp(ip);
//				String json = JSON.toJSONString(vo);
//				PersonPositionWebSocket.sendMessage(json, ip);
//			}
		}

		MDC.put("ip", ip);
		// 离线时根据ip查询离线设备,再将设备状态改为offline,同时修改更新时间
		PersonPositionDevice dev = personPositionDeviceService.getOneByIp(ip);
		if(dev != null) {
			dev.setDeviceState("offline");
			dev.setUpdateTime(new Date());
			personPositionDeviceService.modify(dev);
		}
		logger.info("人员定位设备已断开{},设备状态为{}",ip,dev.getDeviceState());
		MDC.remove("ip");

//		NetMapping.getInstance().removeChannel(ctx.channel());
//		NetMapping.getInstance().removeChannelMapping(ip);

		try {
			this.messageHandleContent.clearHandleClass();
		} catch (Exception e) {
			logger.error(e);
		} finally {
//			personPositionService.offline(ip); // 记录设备下线状态
		}
		super.channelInactive(ctx);
	}

	
	@NotProguard
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PersonPositionMessage msg) throws Exception {	
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		
		byte[] data = msg.getData(); // 接收设备完整回复的指令
		byte[] deviceIdArray = msg.getDevId(); // 单独复制设备编号指令段以便查询,从而修改状态,deviceId两字节
		
		if(msg.getCommand() == 0x44) { //0x44协议用来读取设备配置
			System.out.println("==========设备回复指令的协议是:" + msg.getCommand() + " " + "Data段是:" + ByteUtil.byteArrToHexString(data));
			
			// 一旦设备连接,查到设备则更新设备状态、更新时间、ip,然后存到数据库,没查到则插入一条新数据
			PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			logger.info("连接的设备编号是{}",ByteUtil.byteArrToHexString(deviceIdArray));
			if(dev != null){
				dev.setDeviceState("online");
				dev.setIp(ip);
				dev.setUpdateTime(new Date());
			} else {
				PersonPositionDevice newDevice = new PersonPositionDevice();
				newDevice.setDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
				newDevice.setDeviceState("online");
				newDevice.setCreateTime(new Date());
				newDevice.setUpdateTime(new Date());
				newDevice.setIp(ip);
				personPositionDeviceService.insert(newDevice);
			}
			
			//将配置类放入内存
			DeviceConfigReadVo dconfig = new DeviceConfigReadVo();
			dconfig.setTagType(data[11]); //11是指令Data[]段,含标签类型
			deviceCodeService.add(msg,dconfig);
			if(dconfig.getTagType() == 30){ //30是标签协议编号,目前设备只能读取标签30
				System.out.println("===========发送指令的设备编号是:" + ByteUtil.byteArrToHexString(msg.getDevId()) + " " + "本次读取的标签类型为:" + dconfig.getTagType());
			}
			else {
				System.out.println("===========发送指令的设备编号是:" + ByteUtil.byteArrToHexString(msg.getDevId()) + " " + "未识别读取的标签类型为:" + dconfig.getTagType());
			}

		} else if(msg.getCommand() == 0x41){ //0x41协议用来读取标签
			String deviceId = ByteUtil.byteArrToHexString(msg.getDevId());
			int tagType = deviceCodeService.findByDeviceId(deviceId).getTagType();
			System.out.println("该指令读到的标签类型是:" + tagType);
			List<DeviceTagReadVo> tags = null;
			if(tagType == 0 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6){
				ITagDecoder td = new Tag0Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 1){
				ITagDecoder td = new Tag1Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 10){
				ITagDecoder td = new Tag10Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 11){
				ITagDecoder td = new Tag11Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 20){
				ITagDecoder td = new Tag20Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 30){
				ITagDecoder td = new Tag30Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 251){
				ITagDecoder td = new Tag251Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 252){
				ITagDecoder td = new Tag252Decoder();
				tags = td.decoder(msg);
			} else if(tagType == 253){
				ITagDecoder td = new Tag253Decoder();
				tags = td.decoder(msg);
			}
			
			// TODO 发送tag到MQ
			
		} else if(msg.getCommand() == 0x42) { //0x42协议接收设备心跳
			String deviceId = ByteUtil.byteArrToHexString(msg.getDevId());
			
			
			
			
		} else {
			System.out.println("==========未收到读取设备配置指令");
		}
		
		// TODO 这里添加数量判断 linkDeviceCount
		if (this.deviceIndex > 5 && PersonPositionResponseHandleContext.linkDeviceCount > 5) {

		}
		Object handleResultObj = null;
		if (msg == null)
			return;
		try {
			handleResultObj = this.messageHandleContent.handle(ctx, msg);
			logger.info("handleResultObj:{}", JSON.toJSONString(handleResultObj));
		} finally {
//			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//			String ip = insocket.getAddress().getHostAddress();
//
//			PersonPositionMessageVo vo = PersonPositionMessageVoFactory.create(msg);
//			vo.setIp(ip);
//			if(handleResultObj!=null) {
////				logger.info("if(handleResultObj!=null)");
//				if(handleResultObj instanceof String || 
//						handleResultObj instanceof Integer || 
//						handleResultObj instanceof Float ||
//						handleResultObj instanceof Double ) {
//					vo.setDatas(String.valueOf(handleResultObj));
////					logger.info("vo.setDatas(String.valueOf(handleResultObj));");
//				}else {
//					vo.setDatas(JSON.toJSONString(handleResultObj));
////					logger.info("vo.setDatas(JSON.toJSONString(handleResultObj));");
//				}
//			}
//			String json = JSON.toJSONString(vo);
//			String command = ByteUtil.byteArrToHexString(new byte[] { msg.getCommand() });
//			MDC.put("ip", ip);
//			MDC.put("hid", ByteUtil.byteArrToHexString(msg.getDevId()));
//			MDC.put("command", PersonPositionMessage.getCommandName(msg.getCommand()) + "[" + command + "]");
//			logger.info("获取到人员定位设备上传数据:{}", json);
//			MDC.remove("ip");
//			MDC.remove("hid");
//			MDC.remove("command");
//			// 如果前端网页websocket的连接人数大与0，则解析收到的协议并发送到前端.
//			if (PersonPositionWebSocket.getOnlineCount() > 0) {
//				vo.setMode(PersonPositionMessageVo.RECEIVE_MODE);
//				PersonPositionWebSocket.sendMessage(json, ip);
//			}
		}

	}
}
