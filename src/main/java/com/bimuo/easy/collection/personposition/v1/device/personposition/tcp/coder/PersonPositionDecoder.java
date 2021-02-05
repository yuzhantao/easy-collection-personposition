package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.coder;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 硬件回复的消息预处理(解码及规范大格式)
 * 
 * @author Pingfan
 *
 */
public class PersonPositionDecoder extends LengthFieldBasedFrameDecoder {
//	private static Logger logger = LogManager.getLogger(PersonPositionDecoder.class.getName());

	// 非数据段长度(不含校验码) 0x02 0x03 0x04 0x05
	private static final int HEADER_SIZE = 10;

	// 校验码长度
	private static final int CHECK_SIZE = 1;

	/**
	 * 协议头 4字节 包头，固定值，0x02030405
	 */
	private byte header;
	/**
	 * 数据长度 2字节 数据长度，LENGTH=LEN+DEVID+CMD+SN+DATA+CHECK
	 */
	private short dataLen;
	/**
	 * DATA 长度 DATA=dataLen-7
	 */
	private short realDataLen;
	/**
	 * 设备id 2字节
	 */
	private byte[] devId = new byte[2];
	/**
	 * 命令 1字节 用于区分协议的类型
	 */
	private byte command;
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
	}

	int index = 0;
	@NotProguard
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//		String ip = insocket.getAddress().getHostAddress();
		if (in == null) {
//			logger.info("threadId=" + Thread.currentThread().getId() + "return null;");
			return null;
		}
		in.markReaderIndex(); // 设置读标记
		
		// 直接打印收到的指令
		byte[] allData = new byte[in.readableBytes()];
		in.readBytes(allData);
//		System.out.println("硬件的指令是：" + ByteUtil.byteArrToHexString(allData));
		in.resetReaderIndex(); // 还原到读标记位置
		
		if (this.data != null) { // body不等于空说明上一次读到完整数据了
			// 数据不完整直接舍弃
//			System.out.println("缓存里的指令长度："+in.readableBytes());
//			System.out.println("非数据段的长度(不含校验位)："+HEADER_SIZE);
			if (in.readableBytes() < HEADER_SIZE) {
//				System.out.println("读取的缓存里的指令长度："+in.readableBytes());
//				System.out.println("读取的非数据段的长度(不含校验位)："+HEADER_SIZE);
				return null;
			}
			
			// 按字节读包头
			this.header = in.readByte();
			if (this.header != Integer.valueOf("02", 16).byteValue()) { // 判断包头
//				System.out.println("02包头不合法,舍弃");
				return null;
			}
			this.header = in.readByte();
			if (this.header != Integer.valueOf("03", 16).byteValue()) { // 判断包头
//				System.out.println("03包头不合法,舍弃");
				return null;
			}
			this.header = in.readByte();
			if (this.header != Integer.valueOf("04", 16).byteValue()) { // 判断包头
//				System.out.println("04包头不合法,舍弃");
				return null;
			}
			this.header = in.readByte();
			if (this.header != Integer.valueOf("05", 16).byteValue()) { // 判断包头
//				System.out.println("05包头不合法,舍弃");
				return null;
			}
			
			// 读其它段
			this.dataLen = in.readShort(); // 数据长度，2个字节
//			System.out.println("指令长度是："+dataLen);
			
			in.readBytes(this.devId); // 设备编号
//			System.out.println("设备编号是："+ByteUtil.byteArrToHexString(devId));
			
			this.command = in.readByte(); // 命令
//			System.out.println("命令编号是："+command);
			
			this.sn = in.readByte(); // 数据包自增量
//			System.out.println("流水号是："+sn);
			
			// 根据 LENGTH=LEN+DEVID+CMD+SN+DATA+CHECK，DATA长度=DATALEN-LEN-DEVID-CMD-SN-CHECK
			// 计算 DATA长度
			this.realDataLen = (short) (this.dataLen - 11);
		}
		
//		System.out.println("缓存里的指令长度："+in.readableBytes());
//		System.out.println("实际的数据长度是：" + this.realDataLen);
//		System.out.println("校验位长度是：" + CHECK_SIZE);
		if (in.readableBytes() < this.realDataLen + CHECK_SIZE) {
//			System.out.println("不合法的数据是："+ ByteUtil.byteArrToHexString(this.data));
			this.data = null;
			return null;
		} else {
			// 读数据位
			this.data = new byte[this.realDataLen];
			if (this.realDataLen > 0) {
				in.readBytes(this.data);
//				System.out.println("合法的数据是："+ ByteUtil.byteArrToHexString(this.data));
			}
			
			// 读校验位
			byte check = in.readByte();
//			System.out.println("采集读到的校验码是：" + ByteUtil.byteArrToHexString(new byte[] {check}));
	        // 计算校验位
	        byte crc = 0;
	        in.resetReaderIndex(); // 还原到读标记位置
	        int sumData = dataLen-1;
//	        System.out.println("累加的数据长度是：" + sumData);
	        for(int i = 0; i < sumData; i++) {
	        	crc+=in.readByte();
	        }
//	        System.out.println("采集计算的校验码是：" + ByteUtil.byteArrToHexString(new byte[] {crc}));
	        
	        if (crc!=check) {
				// TODO 当硬件传来的数据有问题需要循环解码，暂时返回空
	        	//	去掉头部,重新解码
	        	//in.setIndex(4, in.writerIndex());
//	        	System.out.println("=========校验失败 源数据crc="+check+"   计算crc="+crc  );
	        	return null;
	        } else {
//	        	System.out.println("=========校验成功");
	        	in.readByte(); // 未声明校验位变量,为使下一次指令从头开始读取,需要重读校验位
			}
		}
		PersonPositionMessage message = new PersonPositionMessage(this.devId,this.command,this.sn,this.data);
		return message;
	}
}