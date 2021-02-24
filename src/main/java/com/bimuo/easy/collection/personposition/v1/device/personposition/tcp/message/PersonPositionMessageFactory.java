package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
/**
 * 消息工厂 生成发送给硬件的指令
 * 
 * @author yuzhantao
 *
 */
public class PersonPositionMessageFactory {
	private final static short HEADER1=0x02;	// 协议头
	private final static short HEADER2=0x03;	// 协议头
	private final static short HEADER3=0x04;	// 协议头
	private final static short HEADER4=0x05;	// 协议头
	private static byte sn;
	public static byte[] createMessage(byte[] deviceId,byte cmd,byte[] datas) {
		byte[] msg = new byte[datas.length+11];
		// 设置协议头
		msg[0]=(byte)HEADER1;
		msg[1]=(byte)HEADER2;
		msg[2]=(byte)HEADER3;
		msg[3]=(byte)HEADER4;
		System.arraycopy(ByteUtil.shortToByteArr((short)(msg.length-4)), 0, msg, 4, 2);
		System.arraycopy(deviceId,0,msg,6,2);
		msg[8]=cmd;
		msg[9]=(++sn);
		System.arraycopy(datas,0,msg,10,datas.length);
		byte crc=0;
		for(int i=0;i<msg.length-1;i++) {
			crc+=msg[i];
		}
		msg[msg.length-1]=crc;
		return msg;
	}
}
