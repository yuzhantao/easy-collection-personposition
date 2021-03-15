package com.bimuo.easy.collection.personposition.v1.service.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;

public class GetNetworkParamsCommandTest {

	private PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	public static void main(String[] args) {
		byte[] data = {0x41};
		byte[] networkParamsDatas = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes("0058"), (byte) 0x47, data);  // 读取网络参数
		System.out.println(ByteUtil.byteArrToHexString(networkParamsDatas,true));
		//{0x02,0x03,0x04,0x05,0x00,0x0C,0x00,0x58,0x47,0x00,0x41,(byte) 0xFA};正确指令
	}
}
