package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.message.IMessageHandle;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;
import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceBaseConfigVo;

import io.netty.channel.ChannelHandlerContext;

/**
 * 读取下位机上传的标签信息的类
 * 
 * @author Pingfan
 *
 */
public class LabelReadHandle implements IMessageHandle<PersonPositionMessage, Object> {
	protected static Logger logger = LogManager.getLogger(LabelReadHandle.class.getName());
	protected static byte ACTION_CODE = Integer.valueOf("41", 16).byteValue();
	protected static final String NULL_TAG_ID = "00000000"; // 空标签ID
	protected final static String EXCEPTION_TAG_ID = "FFFFFFFF"; // 异常标签ID
	private static final PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	private PersonPositionEventBusService personPositionEventBusService;
	public LabelReadHandle(PersonPositionEventBusService personPositionEventBusService) {
		this.personPositionEventBusService = personPositionEventBusService;
	}

	// 最大标签数量
	private final static int MAX_TAG_COUNT = 54;

	@Override
	public boolean isHandle(PersonPositionMessage t) {
		if (ACTION_CODE == t.getCommand()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Object handle(ChannelHandlerContext ctx, PersonPositionMessage t) {
		//将PersonPositionMessage转换为DeviceConfigReadVo
		DeviceBaseConfigVo configVo = new DeviceBaseConfigVo();
//		//把devId转为deviceCode
		configVo.setDeviceId(ByteUtil.byteArrToHexString(t.getDevId()));
//		//将data转换为list集合,再存到list<Tag>
//		if ((t.getData().length / 5) > MAX_TAG_COUNT) {
//			logger.error("获取的标签数量超过{}个", MAX_TAG_COUNT);
//			return null;
//		}
//
//		if ((t.getData().length) % 5 == 0) { // 因“N个标签数据，每个标签=1byte（U位）+4 byte（UID）” ，此处判断
//												// 数据的长度是不是5的倍数，如果不是暂时认为时客户端传输有误
//			int iCurIdx = 0; // 指针
//			List<Tag> uList = new ArrayList<>();
//			long dataLen = t.getData().length;
//			// 遍历标签
//			while (iCurIdx < dataLen) {
//				String uPosition = String.valueOf(t.getData()[iCurIdx]); // U位
//				String labCode = ByteUtil.byteArrToHexString(t.getData(), (iCurIdx + 1), 4).toUpperCase(); // 标签编号
//				iCurIdx += 5;
//				if (EXCEPTION_TAG_ID.equals(labCode)) { // 如果标签有FFFFFFFF的，认为扫描杆程序异常，将不处理，并且不返回信息。
//					logger.error("有标签编号为[{}],因此本次标签数据不处理，源数据:{}", EXCEPTION_TAG_ID, ByteUtil.byteArrToHexString(t.getData()));
//					return null;
//				} else if (!NULL_TAG_ID.equals(labCode)) {
//					Tag tag = ragVo.new Tag();
//					tag.setU(Integer.parseInt(uPosition));
//					tag.setG(labCode);
//					uList.add(tag);
//				}
//			}
//			ragVo.setData(uList);
//			//发送到数据总线
//			this.PersonPositionEventBusService.post(ragVo);
//		}

//		try {
//			// TODO 因硬件原因，软件端需要屏蔽超过最大U位数量的数据
//			if ((t.getData().length / 5) > MAX_TAG_COUNT) {
//				logger.error("获取的标签数量超过{}个", MAX_TAG_COUNT);
//				return null;
//			}
//
//			if ((t.getData().length) % 5 == 0) { // 因“N个标签数据，每个标签=1byte（U位）+4 byte（UID）” ，此处判断
//													// 数据的长度是不是5的倍数，如果不是暂时认为时客户端传输有误
//				int iCurIdx = 0; // 指针
//				List<UInfoVo> uList = new ArrayList<>();
//				long dataLen = t.getData().length;
//				// 遍历标签
//				while (iCurIdx < dataLen) {
//					String uPosition = String.valueOf(t.getData()[iCurIdx]); // U位
//					String labCode = ByteUtil.byteArrToHexString(t.getData(), (iCurIdx + 1), 4).toUpperCase(); // 标签编号
//					iCurIdx += 5;
//					if (EXCEPTION_TAG_ID.equals(labCode)) { // 如果标签有FFFFFFFF的，认为扫描杆程序异常，将不处理，并且不返回信息。
//						logger.error("有标签编号为[{}],因此本次标签数据不处理，源数据:{}", EXCEPTION_TAG_ID, ByteUtil.byteArrToHexString(t.getData()));
//						return null;
//					} else if (!NULL_TAG_ID.equals(labCode)) {
//						UInfoVo uInfo = new UInfoVo();
//						uInfo.setU(uPosition);
//						uInfo.setTag(labCode);
//						uList.add(uInfo);
//					}
//				}
//				sendMessageToRu2000(ctx, t, (byte) 0, 1); // 上位机回复信息
//				String uListJson = JSON.toJSONString(uList);
//				logger.info("扫描到U位信息 command=80 datas={}", uListJson);
//				this.sendMessageToMQ(ctx, t, uListJson); // 发送消息到MQ
//				return uList;
//			} else {
//				logger.error("RU2000 12号指令返回的正文不是5的倍数！"); // 根据协议，此处应该给下位机返回接受失败的应答
//				sendMessageToRu2000(ctx, t, (byte) 1, 1); // 上位机回复信息
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
		
		return null;
	}

//	private void sendMessageToMQ(ChannelHandlerContext ctx, RU2000Message msg,String uList) {
//		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//		String ip = insocket.getAddress().getHostAddress();
//		String deviceCode = ByteUtil.byteArrToHexString(msg.getHostNumber());
//		messageSender.send(RFID_TOPIC, ip, deviceCode, "tag",uList);
//	}
//	
//	private void sendMessageToRu2000(ChannelHandlerContext ctx, RU2000Message msg, byte state, int length) {
//		byte[] datas = ru2000MessageFactory.createGUISMessage(msg, state, length);
//		ByteBuf bs = Unpooled.copiedBuffer(datas);
//		ChannelFuture cf = ctx.writeAndFlush(bs);
//
//		cf.addListener(new ChannelFutureListener() {
//			@NotProguard
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				if (future.isSuccess()) {
//					logger.debug("发送回复硬件数据命令成功 deviceCoe={} 下发命令={}", ByteUtil.byteArrToHexString(msg.getHostNumber()),
//							ByteUtil.byteArrToHexString(datas, true));
//				} else {
//					logger.error("发送回复硬件数据命令失败 deviceCoe={} 下发命令={}", ByteUtil.byteArrToHexString(msg.getHostNumber()),
//							ByteUtil.byteArrToHexString(datas, true));
//				}
//			}
//
//		});
//	}
}
