package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.MDC;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.message.MessageHandleContext;
import com.bimuo.easy.collection.personposition.core.server.CollectionServer;
import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;
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
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag0Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag10Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag11Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag1Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag20Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag251Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag252Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag253Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag30Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag3Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag4Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag5Vo;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag6Vo;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceIpNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.TagReadNoDeviceException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.model.TagHistory;
import com.bimuo.easy.collection.personposition.v1.mqtt.IMqttMessageSenderService;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceSettingService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceBaseConfigVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceSettingVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.NetworkParamsVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port0Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port1Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port2Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port3Vo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PersonPositionResponseHandleContext extends SimpleChannelInboundHandler<PersonPositionMessage> {
	protected final static Logger logger = LogManager.getLogger(PersonPositionResponseHandleContext.class);
	private MessageHandleContext<PersonPositionMessage, Object> messageHandleContent;
	
	private PersonPositionEventBusService personPositionEventBusService;
	private IDeviceConfigService deviceConfigService; // 解析设备回复的设备配置指令
	private IDeviceSettingService deviceSettingService; // 解析设备回复的总配置指令
	private IPersonPositionDeviceService personPositionDeviceService; // 人员定位相关服务
	private ITagHistoryService tagHistoryService; // 标签历史相关服务
	private IMqttMessageSenderService mqttMessageSenderService; // mqtt相关服务
	
	private static int linkDeviceCount = 0; // 设备总数量
	private int deviceIndex; // 设备索引号
	
	
	public PersonPositionResponseHandleContext(PersonPositionEventBusService personPositionEventBusService, IDeviceConfigService deviceConfigService, IDeviceSettingService deviceSettingService, IPersonPositionDeviceService personPositionDeviceService,ITagHistoryService tagHistoryService,IMqttMessageSenderService mqttMessageSenderService) {
		super();
		this.personPositionEventBusService = personPositionEventBusService;
		this.deviceConfigService = deviceConfigService;
		this.deviceSettingService = deviceSettingService;
		this.personPositionDeviceService = personPositionDeviceService;
		this.tagHistoryService = tagHistoryService;
		this.mqttMessageSenderService = mqttMessageSenderService;
		this.messageHandleContent = new MessageHandleContext<>();
//		this.messageHandleContent.setOnlyHandle(false);
//		
//		this.messageHandleContent.addHandleClass(new LabelReadHandle(PersonPositionEventBusService)); // 读取下位机上传的标签信息的类
//		this.messageHandleContent.addHandleClass(new UScanChangeUploadULocationMegHandle(PersonPositionEventBusService)); // 处理下位机在U位信息有变化时主动上传U位扫描信息的类
		//this.messageHandleContent.addHandleClass(new HeartPackageHandle()); // 下位机发送心跳包
//		this.messageHandleContent.addHandleClass(new TemperatureHandle(PersonPositionEventBusService)); // 温度处理
//		this.messageHandleContent.addHandleClass(new OnlineHandle());
//		this.messageHandleContent.addHandleClass(new OfflineHandle());
	}

	@NotProguard
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		CollectionServer.channels.add(ctx.channel());
		this.deviceIndex = ++linkDeviceCount;
		// GetPersonPositionDeviceConfigTask替代下述代码
//	    byte[] datas = {0x02,0x03,0x04,0x05,0x00,0x0B,0x00,0x58,0x44,0x00,(byte) 0xB5};
//		ByteBuf bs = Unpooled.copiedBuffer(datas);
//		ChannelFuture cf = ctx.writeAndFlush(bs);  
//	    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();    
//		// 回调函数监听是否发送成功
//		cf.addListener(new ChannelFutureListener() {
//			@NotProguard
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				if (future.isSuccess()) {
//					logger.info("发送读取阅读器配置参数命令成功,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
//				} else {
//					logger.error("发送读取阅读器配置参数命令失败,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
//				}
//			}
//		});

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
		CollectionServer.channels.remove(ctx.channel());
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
		List<PersonPositionDevice> devices = personPositionDeviceService.getOfflineDevicesByIp(ip);
//		if(devices.isEmpty()) {
//			logger.error("断开时根据ip查询到多条记录,更新数据库失败!");
//		} else {
//			for (int i = 0; i < devices.size(); i++) {
//				devices.get(i).setDeviceState("offline");
//				devices.get(i).setUpdateTime(new Date());
//				personPositionDeviceService.modify(devices.get(i));
//				logger.error("人员定位【{}】已断开,ip为{},状态为{}",devices.get(i).getDeviceCode(), ip, devices.get(i).getDeviceState());
//				// 离线时删除code-channel映射,以备修改配置使用
//				CodeMapping.getInstance().removeChannelMapping(devices.get(i).getDeviceCode());  // 避免断开连接时netty自动将管道清除
//				if(CodeMapping.getInstance().channelMappingContainsKey(devices.get(i).getDeviceCode()) == false) {
//					logger.debug("复位后设备【{}】映射删除成功",devices.get(i).getDeviceCode());
//				} else {
//					logger.error("复位后设备【{}】映射删除失败或并无该记录",devices.get(i).getDeviceCode());
//				}
//			}
//		}
		AssertUtils.checkArgument(devices.isEmpty() == false, new DeviceIpNoneException());
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

	long startTime = System.currentTimeMillis();
	
	@NotProguard
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PersonPositionMessage msg) throws Exception {
		// 取硬件ip地址
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		
		// 添加管道并记录在code-channel映射表,以备修改硬件配置时,根据设备编号查询管道
		// 需要判断连接的设备编号是否在映射表中
		if(CodeMapping.getInstance().channelMappingContainsKey(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase()) == false // 映射表里没有该映射
			|| CodeMapping.getInstance().getChannel(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase()) == null) {          // 映射表里有编号但断线重连时netty将管道删除
			// 新添加code-channel映射
			CodeMapping.getInstance().addChannel(ctx.channel());
			CodeMapping.getInstance().addChannelMapping(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(),ctx.channel());
			logger.info("新添加code-channel映射,设备编号={},管道={}",ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(),ctx.channel());
			// 更新DeviceConfigResponse映射表
//			if(StringUtils.isNotBlank(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase())) {
//				DeviceConfigResponseMapping.getInstance().addResponseMapping(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(), "updatedAndReset");
//				logger.info("更新DeviceConfigResponse表,编号{},状态{}",ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(),DeviceConfigResponseMapping.getInstance().getUpdateConfigState(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase()));
//			} else {
//				logger.error("新修改的配置中设备编号为空!DeviceConfigResponse映射表更新失败!");
//			}
		} else {
			logger.debug("code-channel表中存在该映射,设备编号={},管道={}",ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(),CodeMapping.getInstance().getChannel(ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase()));
		}
		
		byte[] data = msg.getData(); // 接收设备完整回复的指令
		
		byte[] deviceIdArray = msg.getDevId(); // 单独复制设备编号指令段以便查询,从而修改状态,deviceId两字节
		
		if(msg.getCommand() == 0x43) { // 0x43协议用来修改设备配置
			// 设置成功则设备回复:Data[] = 'OK(4F4B)',失败没反应,CheckSum为任意值
			if(ByteUtil.byteArrToHexString(data).equals("4F4B")) {
				// 使用map记录修改配置后的编号,以备修改配置的Controller读取设备回复的消息
//				DeviceConfigResponseMapping.getInstance().addResponseMapping(ByteUtil.byteArrToHexString(deviceIdArray), "updatedNoReset");
//				logger.info("新添加DeviceConfigResponse表的编号{},状态{}",ByteUtil.byteArrToHexString(deviceIdArray),DeviceConfigResponseMapping.getInstance().getUpdateConfigState(ByteUtil.byteArrToHexString(deviceIdArray)));
				logger.info("硬件回复OK-【{}】人员定位【修改基础配置】成功!",ByteUtil.byteArrToHexString(deviceIdArray));
				// 设置原记录无效
				PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
				if(dev != null && dev.isEffective()) {
					dev.setEffective(false);
					personPositionDeviceService.modify(dev);
				} else {
					logger.error("修改配置时,设置原记录无效失败!");
				}
				
//				// TODO 修改后复位逻辑待修改
				// 硬件修改成功,发送复位指令
				byte[] command = {0x02,0x03,0x04,0x05,0x00,0x13,0x00,(byte) 0x88,0x61,0x00,0x4D,0x43,0x55,0x52,0x45,0x53,0x45,0x54,0x72};
				Channel channel = CodeMapping.getInstance().getChannel(ByteUtil.byteArrToHexString(deviceIdArray));
				if(channel == null) {
					logger.error("code-channel表中不存在设备编号【{}】的管道",ByteUtil.byteArrToHexString(deviceIdArray));
				} else {
					logger.debug("设备编号【{}】对应的管道是{}",ByteUtil.byteArrToHexString(deviceIdArray),channel);
					ByteBuf bs = Unpooled.copiedBuffer(command);
					ChannelFuture cf = channel.writeAndFlush(bs);
					// 回调函数监听是否发送成功
					cf.addListener(new ChannelFutureListener() {
						@NotProguard
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if (future.isSuccess()) {
								logger.info("发送复位命令成功,下发命令={}",ByteUtil.byteArrToHexString(command, true));
							} else {
								logger.error("发送复位命令失败,下发命令={}",ByteUtil.byteArrToHexString(command, true));
							}
						}
					});
				}
			} else {
				logger.error("设备编号【{}】修改硬件配置失败! Data段是【{}】",ByteUtil.byteArrToHexString(deviceIdArray),ByteUtil.byteArrToHexString(data));
			}
		}
		
		if (msg.getCommand() == 0x46) { // 0x46协议用来修改网络参数/端口配置
			// 设置成功则设备回复:Data[] = 'OK(4F4B)',失败没反应,CheckSum为任意值
			if(ByteUtil.byteArrToHexString(data).equals("4F4B")) {
			logger.info("硬件回复OK-【{}】修改硬件网络参数/端口成功!",ByteUtil.byteArrToHexString(deviceIdArray));
			// 设置原记录无效
			PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			if(dev != null && dev.isEffective()) {
				dev.setEffective(false);
				personPositionDeviceService.modify(dev);
			} else {
				logger.error("修改网络参数/端口时,设置原记录无效失败!");
			}

			// TODO 修改后复位逻辑待修改
			// 硬件修改成功,发送复位指令
			byte[] command = {0x02,0x03,0x04,0x05,0x00,0x13,0x00,(byte) 0x88,0x61,0x00,0x4D,0x43,0x55,0x52,0x45,0x53,0x45,0x54,0x72};
			Channel channel = CodeMapping.getInstance().getChannel(ByteUtil.byteArrToHexString(deviceIdArray));
			if(channel == null) {
				logger.info("code-channel表中不存在设备编号【{}】的管道",ByteUtil.byteArrToHexString(deviceIdArray));
			} else {
				logger.info("设备编号【{}】对应的管道是{}",ByteUtil.byteArrToHexString(deviceIdArray),channel);
				ByteBuf bs = Unpooled.copiedBuffer(command);
				ChannelFuture cf = channel.writeAndFlush(bs);
					// 回调函数监听是否发送成功
					cf.addListener(new ChannelFutureListener() {
						@NotProguard
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if (future.isSuccess()) {
								logger.info("发送复位命令成功,下发命令={}",ByteUtil.byteArrToHexString(command, true));
							} else {
								logger.error("发送复位命令失败,下发命令={}",ByteUtil.byteArrToHexString(command, true));
							}
						}
					});
				}
			} else {
				logger.error("设备编号【{}】修改网络参数/端口失败! Data段是【{}】",ByteUtil.byteArrToHexString(deviceIdArray),ByteUtil.byteArrToHexString(data));
			}
		}
		
		if (msg.getCommand() == 0x47) { // 0x47协议用来读取网络参数/端口配置
			logger.debug("==========设备回复指令的协议是:" + msg.getCommand() + " " + "Data段是:" + ByteUtil.byteArrToHexString(data));
			// 1.处理设备信息
			// 一旦设备连接,查到设备则更新数据库设备状态、更新时间、ip,然后存到数据库,没查到则插入一条新数据
			PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			logger.debug("==========连接的设备编号是{}", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			if (dev != null) {
				dev.setDeviceState("online");
				dev.setIp(ip);
				dev.setUpdateTime(new Date());
				dev.setDeviceType("人员定位");
				dev.setEffective(true);
				personPositionDeviceService.modify(dev);
				logger.debug("==========数据库设备信息更新成功!");
			} else {
				PersonPositionDevice newDevice = new PersonPositionDevice();
				newDevice.setDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
				newDevice.setDeviceState("online");
				newDevice.setCreateTime(new Date());
				newDevice.setUpdateTime(new Date());
				newDevice.setDeviceType("人员定位");
				newDevice.setIp(ip);
				newDevice.setEffective(true);
				personPositionDeviceService.insert(newDevice);
				logger.debug("==========数据库添加新设备成功!");
			}
			
			// 2.处理设备总配置信息
			PersonPositionDevice device = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			// 部分更新数据库总配置信息,修改网络参数以及端口,以二级指令INS区分
			if(data[0] == 0x41) { // INS二级指令为A,表示网络参数指令
				// 解析设备回复的网络参数
				byte[] sourceIpArr = new byte[4];
				System.arraycopy(data, 1, sourceIpArr, 0, 4);
				byte[] subnetMaskArr = new byte[4];
				System.arraycopy(data, 5, subnetMaskArr, 0, 4);
				byte[] gatwayArr = new byte[4];
				System.arraycopy(data, 9, gatwayArr, 0, 4);
				byte[] sourceHardwareArr = new byte[6];
				System.arraycopy(data, 13, sourceHardwareArr, 0, 6);
				NetworkParamsVo networkParams = deviceSettingService.updateNetworkParams(
						ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(), 
						sourceIpArr, subnetMaskArr, gatwayArr, sourceHardwareArr);
				// 更新总配置来更新数据库网络参数
				if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
					DeviceSettingVo dsetting = new DeviceSettingVo();
					dsetting.setNetworkParams(networkParams);
					device.setDeviceSetting(dsetting);
				} else {
					device.getDeviceSetting().setNetworkParams(networkParams);
				}
				personPositionDeviceService.modify(device);
			} else if(data[0] == 0x42) { // INS二级指令为B(42)表示端口0
				// 解析设备回复的端口0配置
				byte[] socket0DIPArr = new byte[4];
				System.arraycopy(data, 1, socket0DIPArr, 0, 4);
				byte[] DPortArr = new byte[2];
				System.arraycopy(data, 5, DPortArr, 0, 2);
				byte[] SPortArr = new byte[2];
				System.arraycopy(data, 7, SPortArr, 0, 2);
				Port0Vo port0 = deviceSettingService.updatePort0(
						ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(),
						socket0DIPArr, ByteUtil.byteArrToShort(DPortArr), 
						ByteUtil.byteArrToShort(SPortArr),data[9],data[10]);
				// 更新总配置来更新数据库端口0
				if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
					DeviceSettingVo dsetting = new DeviceSettingVo();
					dsetting.setPort0(port0);
					device.setDeviceSetting(dsetting);
				} else {
					device.getDeviceSetting().setPort0(port0);
				}
				personPositionDeviceService.modify(device);
			} else if(data[0] == 0x43) { // INS二级指令为C(43)表示端口1
				// 解析设备回复的端口1配置
				byte[] socket0DIPArr = new byte[4];
				System.arraycopy(data, 1, socket0DIPArr, 0, 4);
				byte[] DPortArr = new byte[2];
				System.arraycopy(data, 5, DPortArr, 0, 2);
				byte[] SPortArr = new byte[2];
				System.arraycopy(data, 7, SPortArr, 0, 2);
				Port1Vo port1 = deviceSettingService.updatePort1(
						ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(), 
						socket0DIPArr, ByteUtil.byteArrToShort(DPortArr), 
						ByteUtil.byteArrToShort(SPortArr),data[9],data[10]);
				// 更新总配置来更新数据库端口1
				if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
					DeviceSettingVo dsetting = new DeviceSettingVo();
					dsetting.setPort1(port1);
					device.setDeviceSetting(dsetting);
				} else {
					device.getDeviceSetting().setPort1(port1);
				}
				personPositionDeviceService.modify(device);
			} else if(data[0] == 0x44) { // INS二级指令为D(44)表示端口2
//				// 解析设备回复的端口2配置
//				byte[] socket0DIPArr = new byte[4];
//				System.arraycopy(data, 1, socket0DIPArr, 0, 4);
//				byte[] DPortArr = new byte[2];
//				System.arraycopy(data, 5, DPortArr, 0, 2);
//				byte[] SPortArr = new byte[2];
//				System.arraycopy(data, 7, SPortArr, 0, 2);
//				Port2Vo port2 = deviceSettingService.updatePort2(
//						ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(), 
//						socket0DIPArr, ByteUtil.byteArrToShort(DPortArr), 
//						ByteUtil.byteArrToShort(SPortArr),data[9],data[10]);
//				// 更新总配置来更新数据库端口2
//				if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
//					DeviceSettingVo dsetting = new DeviceSettingVo();
//					dsetting.setPort2(port2);
//					device.setDeviceSetting(dsetting);
//				} else {
//					device.getDeviceSetting().setPort2(port2);
//				}
//				personPositionDeviceService.modify(device);
			} else if(data[0] == 0x45) { // INS二级指令为E(45)表示端口3
//				// 解析设备回复的端口3配置
//				byte[] socket0DIPArr = new byte[4];
//				System.arraycopy(data, 1, socket0DIPArr, 0, 4);
//				byte[] DPortArr = new byte[2];
//				System.arraycopy(data, 5, DPortArr, 0, 2);
//				byte[] SPortArr = new byte[2];
//				System.arraycopy(data, 7, SPortArr, 0, 2);
//				Port3Vo port3 = deviceSettingService.updatePort3(
//						ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(), 
//						socket0DIPArr, ByteUtil.byteArrToShort(DPortArr), 
//						ByteUtil.byteArrToShort(SPortArr),data[9],data[10]);
//				// 更新总配置来更新数据库端口3
//				if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
//					DeviceSettingVo dsetting = new DeviceSettingVo();
//					dsetting.setPort3(port3);
//					device.setDeviceSetting(dsetting);
//				} else {
//					device.getDeviceSetting().setPort3(port3);
//				}
//				personPositionDeviceService.modify(device);
			} else {
				logger.error("======未识别设备回复的网络配置信息,协议为47");
			}	
		}
		if (msg.getCommand() == 0x4D) { // 0x4D协议用来更改密钥1
			if(ByteUtil.byteArrToHexString(data).equals("4F4B")) {
				logger.info("设备编号【{}】更改密钥1成功!", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			} else {
				logger.error("设备编号【{}】更改密钥1失败! Data段是【{}】", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase(), ByteUtil.byteArrToHexString(data));
			}
		}
		if (msg.getCommand() == 0x4E) { // 0x4E协议用来更改密钥1
			if(ByteUtil.byteArrToHexString(data).equals("4F4B")) {
				logger.info("设备编号【{}】更改密钥2成功!", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			} else {
				logger.error("设备编号【{}】更改密钥2失败! Data段是【{}】", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase(), ByteUtil.byteArrToHexString(data));
			}
		}
		if (msg.getCommand() == 0x4F) { // 0x4F协议用来更改密钥1
			if(ByteUtil.byteArrToHexString(data).equals("4F4B")) {
				logger.info("设备编号【{}】更改密钥3成功!", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			} else {
				logger.error("设备编号【{}】更改密钥3失败! Data段是【{}】", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase(), ByteUtil.byteArrToHexString(data));
			}
		}
		if (msg.getCommand() == 0x44) { // 0x44协议用来读取设备配置
			logger.debug("==========设备回复指令的协议是:" + msg.getCommand() + " " + "Data段是:" + ByteUtil.byteArrToHexString(data));
			// 1.处理设备信息
			// 一旦设备连接,查到设备则更新数据库设备状态、更新时间、ip,然后存到数据库,没查到则插入一条新数据
			PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			logger.debug("==========连接的设备编号是{}", ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			if (dev != null) {
				dev.setDeviceState("online");
				dev.setIp(ip);
				dev.setUpdateTime(new Date());
				dev.setDeviceType("人员定位");
				dev.setEffective(true);
				personPositionDeviceService.modify(dev);
				logger.debug("==========数据库设备信息更新成功!");
			} else {
				PersonPositionDevice newDevice = new PersonPositionDevice();
				newDevice.setDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
				newDevice.setDeviceState("online");
				newDevice.setCreateTime(new Date());
				newDevice.setUpdateTime(new Date());
				newDevice.setDeviceType("人员定位");
				newDevice.setIp(ip);
				newDevice.setEffective(true);
				personPositionDeviceService.insert(newDevice);
				logger.debug("==========数据库添加新设备成功!");
			}
			
			// 2.处理设备配置信息
			// 更新数据库配置信息,连接读取配置时设备编号既是旧编号也是新编号
			DeviceBaseConfigVo dconfig = deviceConfigService.updateBaseConfig(
					ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase(),
					ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase(), // 设备id,回复的消息msg和Data[]段都有,msg取较方便
					data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], 
					data[11], // 标签类型,11是硬件回复44协议Data[]段的11字节,后根据此字段读取标签类型
					data[12]);
			
			// 更新配置
			PersonPositionDevice device = personPositionDeviceService.getOneByDeviceCode(ByteUtil.byteArrToHexString(deviceIdArray).toUpperCase());
			DeviceSettingVo dsetting = device.getDeviceSetting();
			dsetting.setBaseConfig(dconfig);
			device.setDeviceSetting(dsetting);
			personPositionDeviceService.modify(device);
			
			// 发送配置到mqtt(修改完配置后刷新页面使用)
			String topic = "personpositon/"+ ByteUtil.byteArrToHexString (deviceIdArray).toUpperCase() + "/config";
			String mqttData = JSONArray.toJSON(dconfig).toString();
			if(StringUtils.isNotBlank(topic)) {
				mqttMessageSenderService.sendToMqtt(topic, mqttData);
				logger.debug("发送mqtt消息完成,主题:{},消息:{}", topic, mqttData);
			} else {
				logger.error("发送mqtt消息失败,主题:{},消息:{}", topic, mqttData);
			}
			
			// 3.将配置类放入内存
			deviceConfigService.addMemory(msg, dconfig);

			if (dconfig.getTagType() == 30) { // 30是标签协议编号,目前设备只能读取标签30
				logger.debug("===========发送指令的设备编号是:" + ByteUtil.byteArrToHexString(msg.getDevId()) + " "
						+ "本次读取的标签类型为:" + dconfig.getTagType());
			} else {
				logger.debug("===========发送指令的设备编号是:" + ByteUtil.byteArrToHexString(msg.getDevId()) + " "
						+ "未识别读取的标签类型为:" + dconfig.getTagType());
			}
			
		} else if (msg.getCommand() == 0x41) { // 0x41协议用来读取标签
			String deviceId = ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase();
			if(deviceConfigService.findByDeviceId(deviceId)== null) {
				return;
			}
			AssertUtils.checkArgument(deviceConfigService.findByDeviceId(deviceId) != null, new TagReadNoDeviceException());
			int tagType = deviceConfigService.findByDeviceId(deviceId).getTagType();
			logger.debug("该指令读到的标签类型是:" + tagType);
			
			TagHistory tagHistory = new TagHistory(); // 标签历史,用于存到数据库以及页面展示
			tagHistory.setCreateTime(new Date());
			tagHistory.setDeviceCode(deviceId);
			
			List<DeviceTagReadVo> tags = new ArrayList<>();
			if (tagType == 0 || tagType == 3 || tagType == 4 || tagType == 5 || tagType == 6) {
				ITagDecoder td = new Tag0Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 1) {
				ITagDecoder td = new Tag1Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 10) {
				ITagDecoder td = new Tag10Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 11) {
				ITagDecoder td = new Tag11Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 20) {
				ITagDecoder td = new Tag20Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 30) {
				ITagDecoder td = new Tag30Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 251) {
				ITagDecoder td = new Tag251Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 252) {
				ITagDecoder td = new Tag252Decoder();
				tags = td.decoder(msg);
			} else if (tagType == 253) {
				ITagDecoder td = new Tag253Decoder();
				tags = td.decoder(msg);
			}
			// 标签历史存储扩展属性的map
			Map<String,Object> tagExtendParamsMap = new HashedMap<String, Object>();
			// 1.标签历史存到数据库
			for(int i = 0; i < tags.size(); i++){
				if(tags.get(i) instanceof Tag0Vo) {
					Tag0Vo tag0 = (Tag0Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag0.getTagId()));
					tagHistory.setTagType(tag0.getTagType());
					tagExtendParamsMap.put("标签0","不含任何特征位");
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag3Vo) {
					Tag3Vo tag3 = (Tag3Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag3.getTagId()));
					tagHistory.setTagType(tag3.getTagType());
					tagExtendParamsMap.put("标签3","不含任何特征位");
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag4Vo) {
					Tag4Vo tag4 = (Tag4Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag4.getTagId()));
					tagHistory.setTagType(tag4.getTagType());
					tagExtendParamsMap.put("标签4","不含任何特征位");
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag5Vo) {
					Tag5Vo tag5 = (Tag5Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag5.getTagId()));
					tagHistory.setTagType(tag5.getTagType());
					tagExtendParamsMap.put("标签5","不含任何特征位");
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag6Vo) {
					Tag6Vo tag6 = (Tag6Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag6.getTagId()));
					tagHistory.setTagType(tag6.getTagType());
					tagExtendParamsMap.put("标签6","不含任何特征位");
				} else if(tags.get(i) instanceof Tag1Vo) { 
					Tag1Vo tag1 = (Tag1Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag1.getTagId()));
					tagHistory.setTagType(tag1.getTagType());
					tagExtendParamsMap.put("增益", tag1.getGain());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag10Vo) { 
					Tag10Vo tag10 = (Tag10Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag10.getTagId()));
					tagHistory.setTagType(tag10.getTagType());
					tagExtendParamsMap.put("类型", tag10.getRealTagType());
					tagExtendParamsMap.put("电压", tag10.getVoltage());
					tagExtendParamsMap.put("按钮", tag10.getButton());
					tagExtendParamsMap.put("防拆", tag10.getTamper());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag11Vo) { 
					Tag11Vo tag11 = (Tag11Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag11.getTagId()));
					tagHistory.setTagType(tag11.getTagType());
					tagExtendParamsMap.put("电压", tag11.getVoltage());
					tagExtendParamsMap.put("按钮", tag11.getButton());
					tagExtendParamsMap.put("增益", tag11.getGain());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag20Vo) { 
					Tag20Vo tag20 = (Tag20Vo) tags.get(i);
					tagHistory.setTagId(Integer.toHexString(tag20.getTagId()));
					tagHistory.setTagType(tag20.getTagType());
					tagExtendParamsMap.put("类型", tag20.getRealTagType());
					tagExtendParamsMap.put("电压", tag20.getVoltage());
					tagExtendParamsMap.put("防拆", tag20.getTamper());
					tagExtendParamsMap.put("按钮1", tag20.getButton1());
					tagExtendParamsMap.put("按钮2", tag20.getButton2());
					tagExtendParamsMap.put("增益", tag20.getGain());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag30Vo) {
					Tag30Vo tag30 = (Tag30Vo) tags.get(i);
					tagHistory.setTagId(tag30.getTagId());
					tagHistory.setTagType(tag30.getTagType());
					tagExtendParamsMap.put("激活", tag30.getActivate());
					tagExtendParamsMap.put("电压", tag30.getVoltage());
					tagExtendParamsMap.put("防拆", tag30.getTamper());
					tagExtendParamsMap.put("按钮1", tag30.getButton1());
					tagExtendParamsMap.put("按钮2", tag30.getButton2());
					tagExtendParamsMap.put("增益", tag30.getGain());
					tagExtendParamsMap.put("穿越", tag30.getTraverse());
					tagExtendParamsMap.put("激活器ID", tag30.getActivatorId());
					tagExtendParamsMap.put("场强", tag30.getRSSI());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag251Vo) { 
					Tag251Vo tag251 = (Tag251Vo) tags.get(i);
					tagHistory.setTagId(Long.toHexString(tag251.getTagId()));
					tagHistory.setTagType(tag251.getTagType());
					tagExtendParamsMap.put("电压", tag251.getVoltage());
					tagExtendParamsMap.put("湿度", tag251.getHum());
					tagExtendParamsMap.put("温度", tag251.getTemp());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag252Vo) { 
					Tag252Vo tag252 = (Tag252Vo) tags.get(i);
					tagHistory.setTagId(Long.toHexString(tag252.getTagId()));
					tagHistory.setTagType(tag252.getTagType());
					tagExtendParamsMap.put("按钮", tag252.getButton());
					tagExtendParamsMap.put("电压", tag252.getVoltage());
					tagExtendParamsMap.put("温度范围", tag252.getSign());
					tagExtendParamsMap.put("温度", tag252.getTemp());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				} else if(tags.get(i) instanceof Tag253Vo) { 
					Tag253Vo tag253 = (Tag253Vo) tags.get(i);
					tagHistory.setTagId(Long.toHexString(tag253.getTagId()));
					tagHistory.setTagType(tag253.getTagType());
					tagExtendParamsMap.put("类型", tag253.getRealTagType());
					tagExtendParamsMap.put("电压", tag253.getVoltage());
					tagExtendParamsMap.put("温度范围", tag253.getSign());
					tagExtendParamsMap.put("温度", tag253.getTemp());
					tagHistory.setTagExtendParams(tagExtendParamsMap);
				}
				tagHistoryService.save(tagHistory);
				logger.debug("存储标签历史成功,标签id={}",tagHistory.getTagId());
			}
			
			// 2.发送标签历史到MQTT 接收地址是配置文件中mqtt.host 默认地址在MqttOutConfig
			String topic = "personpositon/"+ deviceId + "/tags/" + tagHistory.getTagId();
			String mqttData = JSONArray.toJSON(tagHistory).toString();
			
//			//解决mqtt正在过多的发布
//			MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//			MqttAsyncClient mqttAscClient = new MqttAsyncClient(serverURI, clientId);
//			if(mqttConnectOptions.getMaxInflight() - mqttAscClient.getPendingDeliveryTokens().length > 0)
		
			if(StringUtils.isNotBlank(topic)) {
				mqttMessageSenderService.sendToMqtt(topic, mqttData);
				logger.debug("发送mqtt消息完成,主题:{},消息:{}", topic, mqttData);
			} else {
				logger.error("发送mqtt消息失败,主题:{},消息:{}", topic, mqttData);
			}
		} else if (msg.getCommand() == 0x42) { // 0x42协议接收设备心跳
			
		} else {
			long endTime = System.currentTimeMillis();
			long waitTime= (endTime - startTime)/1000;
			logger.error("【{}】已连接{}秒……",ByteUtil.byteArrToHexString(msg.getDevId()).toUpperCase(),waitTime);
		}
		// TODO 这里添加数量判断 linkDeviceCount
		if (this.deviceIndex > 5 && PersonPositionResponseHandleContext.linkDeviceCount > 5) {

		}
		Object handleResultObj = null;
		if (msg == null)
			return;
		try {
			handleResultObj = this.messageHandleContent.handle(ctx, msg);
			logger.debug("handleResultObj:{}", JSON.toJSONString(handleResultObj));
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
