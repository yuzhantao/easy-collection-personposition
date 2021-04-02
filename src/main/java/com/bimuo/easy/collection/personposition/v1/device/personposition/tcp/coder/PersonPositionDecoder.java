package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.coder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 硬件回复的消息预处理(解码及规范大格式)
 * 
 * @author Pingfan
 *
 */
public class PersonPositionDecoder extends LengthFieldBasedFrameDecoder {
	

	private static Logger logger = LogManager.getLogger(PersonPositionDecoder.class.getName());
	
	// 最小指令长度(假设数据段是0)
	private static final int MIN_COMMAND_SIZE = 11;

	// 校验码长度
	private static final int CHECK_SIZE = 1;
	/**
	 * 协议头 4字节 包头，固定值，0x02,0x03,0x04,0x05
	 */
	private byte header;
	/**
	 * 指令长度 2字节 指令长度，cmdLen=HEAD+LEN+DEVID+CMD+SN+DATA+CHECK
	 */
	private short cmdLen;
	/**
	 * DATA长度  dataLen=cmdLen-11
	 */
	private short dataLen;
	/**
	 * 设备id 2字节
	 */
	private byte[] devId = new byte[2];
	/**
	 * 命令 1字节 用于区分协议的类型
	 */
	private byte cmdType;
	/**
	 * 流水号/数据包自增量 1字节 用于检验数据包传输
	 */
	private byte sn;
	/**
	 * 数据 N字节 数据段
	 */
	private byte[] data = new byte[0];

	/**
	 * 
	 * @param maxFrameLength      解码时，处理每个帧数据的最大长度
	 * @param lengthFieldOffset   该帧数据中，存放该帧数据的长度的数据的起始位置
	 * @param lengthFieldLength   记录该帧数据长度的字段本身的长度
	 * @param lengthAdjustment    修改帧数据长度字段中定义的值，可以为负数
	 * @param initialBytesToStrip 解析的时候需要跳过的字节数
	 * @param failFast            为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
	 */
	public PersonPositionDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}

	@NotProguard
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ByteBuf bf = this.internalBuffer();
		bf.release();
		logger.debug("ByteBuf释放成功!");
	}

	// 同时解码多条指令
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//		String ip = insocket.getAddress().getHostAddress();
		
		if (in.readableBytes() < MIN_COMMAND_SIZE) {
			return null;
		}
		List<Object> retObj = new ArrayList<>();
//		logger.debug("ip={}解码前,可读字节={}",ip,in.readableBytes());
		try {
			while(true) {
				Object obj = _decode(ctx,in);
				if(obj == null) {
					continue;
				}else {
					retObj.add(obj);
				}
			}
		} catch(Exception e) {
			
		}
//		in.markReaderIndex();
//		if(in.readableBytes() > 0) {
//			logger.debug("已解指令={},可读字节={}",retObj.size(),in.readableBytes());
//			byte[] allData = new byte[in.readableBytes()];
//			in.readBytes(allData);
//			logger.debug("未解指令：" + ByteUtil.byteArrToHexString(allData,true));
//			in.resetReaderIndex();
//		}
		if(retObj.size()==0) {
			return null;
		} else {
			return retObj;
		}
	}
	
	int index = 0;

	@NotProguard
//	@Override
	protected Object _decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if(in == null) {
			logger.info("threadId={}" + Thread.currentThread().getId() + "return null;");
			throw new Exception();
//			return null;
		}
		in.markReaderIndex(); // 设置读标记

		// 直接打印收到的指令
//		byte[] allData = new byte[in.readableBytes()];
//		logger.info("解码前可读字节：{}",in.readableBytes());
//		in.readBytes(allData);
//		logger.info("解码完可读字节：{}",in.readableBytes());
//		logger.info("硬件的指令：" + ByteUtil.byteArrToHexString(allData,true));
//		in.resetReaderIndex(); // 还原到读标记位置

//		if (this.data != null) { // body不等于空说明上一次读到完整数据了
		// 数据不完整直接舍弃(不满足最小指令长度)
		if (in.readableBytes() < MIN_COMMAND_SIZE) {
			throw new Exception();
//			return null;
		}

		// 读4字节包头
		int header4 = in.readInt();
		if (header4 != 33752069) { // 33752069是十六进制02030405的十进制
			in.resetReaderIndex();
			in.readByte();
			return null;
		}
		// 按字节读包头
//		this.header = in.readByte();
//		if (this.header != Integer.valueOf("02", 16).byteValue()) { // 判断包头
//			return null;
//		}
//		this.header = in.readByte();
//		if (this.header != Integer.valueOf("03", 16).byteValue()) { // 判断包头
//			return null;
//		}
//		this.header = in.readByte();
//		if (this.header != Integer.valueOf("04", 16).byteValue()) { // 判断包头
//			return null;
//		}
//		this.header = in.readByte();
//		if (this.header != Integer.valueOf("05", 16).byteValue()) { // 判断包头
//			return null;
//		}

		// 读指令长度
		short cmdLen = in.readShort(); // 指令长度, 2字节
		if (cmdLen < MIN_COMMAND_SIZE || cmdLen > 1024) { // 非法数据长度数据不合法
			in.resetReaderIndex();
			in.readBytes(4);
			return null;
		}
		if (in.readableBytes() < cmdLen - 4 - 2) { // 可读字节 < 指令-Head-Length
			in.resetReaderIndex(); // 指令长度不够,等待下一条
			throw new Exception();
		}

		// 读其它段
		byte[] devId = new byte[2];
		in.readBytes(devId); // 设备编号, 2字节
		byte cmdType = in.readByte(); // 指令类型, 1字节
		byte sn = in.readByte(); // 流水号, 1字节

		// 计算 DATA长度
		short dataLen = (short) (cmdLen - MIN_COMMAND_SIZE);

		// 读数据位
		byte[] data = new byte[dataLen];
		in.readBytes(data);

		// 读校验位
		byte check = in.readByte();

		// 计算校验位
		byte crc = 0;
		in.resetReaderIndex(); // 还原到读标记位置
		int sumData = cmdLen - 1;
		for (int i = 0; i < sumData; i++) {
			crc += in.readByte();
		}

		if (crc != check) {
			// TODO 当硬件传来的数据有问题需要循环解码，暂时返回空
			in.resetReaderIndex();
			// TODO 内存泄露
			in.skipBytes(4); // 头部4字节,去掉头部,重新解码(避免非法数据含有合法信息)
			throw new Exception();
//	        return null;
		} else {
			in.readByte(); // 未声明校验位变量,为使下一次指令从头开始读取,需要重读校验位
			PersonPositionMessage message = new PersonPositionMessage(devId, cmdType, sn, data);
			return message;
		}
	}
}