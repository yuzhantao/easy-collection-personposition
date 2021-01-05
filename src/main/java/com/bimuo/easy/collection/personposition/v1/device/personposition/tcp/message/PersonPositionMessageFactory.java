package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
/**
 * 自动应答客户端  工厂（硬件每次发送报文给上位机后，上位机必须自动做出应答，此类和解析业务系统的工厂有区别）
 * @author Pingfan
 *
 */
public class PersonPositionMessageFactory {
	private final static short HEADER1=0x02;	// 协议头
	private final static short HEADER2=0x03;	// 协议头
	private final static short HEADER3=0x04;	// 协议头
	private final static short HEADER4=0x05;	// 协议头
	
	public byte[] createGUISMessage(
		 PersonPositionMessage guis,
		 byte state, 
		 int length
    ) {
		byte[] result = new byte[10];
		// 设置协议头
		result[0]=(byte)HEADER1;
		result[1]=(byte)HEADER2;
		result[2]=(byte)HEADER3;
		result[3]=(byte)HEADER4;
		// 设置长度    先默认是5，如果到时候变了再改这个方法吧，反正长度作为参数传递进来了
		byte[] bLen = ByteUtil.shortToByteArr((short)(length+4));
		System.arraycopy(bLen, 0, result, 2, bLen.length);
		// 设置主机编号
		System.arraycopy(guis.getDevId(), 0, result, 4, 3);
		// 设置命令字
		result[9]=guis.getCommand();
		// 状态，成功0,失败1
		result[10]=state;
		// 设置校验码
		byte crc=0;
		// crc：从包头到数据段累加和模256
		for(int i=0;i<result.length-1;i++) {
			crc+=result[i];
		}
		result[result.length-1]=crc;
		
		return result;
    }
	
}
