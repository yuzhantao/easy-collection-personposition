package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.message.IMessageHandle;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * 下位机发送心跳包
 * 
 * @author 25969
 *
 */
public class HeartPackageHandle implements IMessageHandle<PersonPositionMessage, Object> {
	private static Logger logger = LogManager.getLogger(HeartPackageHandle.class.getName());
//	private TopicSender topicSender = (TopicSender) ContextUtils.getBean("topicSender");
	private static byte ACTION_CODE = Integer.valueOf("42", 16).byteValue();
	private static final String TAG_NULL = "00000000";
	private static final PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	private boolean isFirst=true;	// 是否是首次接收心跳
	
	public HeartPackageHandle() {
	}
	
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
		String devId = ByteUtil.byteArrToHexString(t.getDevId()); // 主机编号
		try {
			if (!TAG_NULL.equals(devId)) {
				sendMessageToPersonPosition(ctx, t, (byte) 0, 1); // 上位机回复信息
			}
//			pushToMq(devId); // 推送心跳包数据
		} catch (Exception e) {
			logger.error("", e.getMessage());
		}
		
		if(this.isFirst) {
//			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//			String ip = insocket.getAddress().getHostAddress();
//			this.personPositionService.online(ip,devId);
			this.isFirst=false;
		}
		return null;
	}

//	/**
//	 * 推送心跳包数据
//	 * 
//	 * @param devId
//	 */
//	private void pushToMq(String devId) {
//		try {
//			mes = new GUISMQMessage();
//			mes.setActioncode("reader030");
//			u = new UInfoEntity();
//			u.setDevAddrCode(devId);
//			mes.setAwsPostdata(u);
//
//			// String json = JSON.toJSONString(mes);
//			logger.debug(JSON.toJSONString(mes));
//			topicSender.send("daioReader", JSON.toJSONString(mes));
//		} finally {
//			mes = null;
//			u = null;
//		}
//	}

	private void sendMessageToPersonPosition(ChannelHandlerContext ctx, PersonPositionMessage msg, byte state, int length) {
//		byte[] datas = personPositionMessageFactory.createGUISMessage(msg, state, length);
//		ByteBuf bs = Unpooled.copiedBuffer(datas);
//		ChannelFuture cf = ctx.writeAndFlush(bs);
//
//		cf.addListener(new ChannelFutureListener() {
//			@NotProguard
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				if (future.isSuccess()) {
//					logger.info("发送回复心跳命令成功 deviceCoe={} 下发命令={}", ByteUtil.byteArrToHexString(msg.getDevId()),
//							ByteUtil.byteArrToHexString(datas, true));
//					//PersonPositionWebSocket.sendMessage(JSON.toJSONString(vo), ip);
//				} else {
//					logger.error("发送回复心跳命令失败 deviceCoe={} 下发命令={}", ByteUtil.byteArrToHexString(msg.getDevId()),
//							ByteUtil.byteArrToHexString(datas, true));
//				}
//			}
//
//		});
	}
}
