package com.bimuo.easy.collection.personposition.v1.service.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;

public class SecretKeyToHardwareTest {
	private static final Logger logger = LogManager.getLogger(SecretKeyToHardwareTest.class);
	
	private static PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	public static void main(String[] args) {
		byte[] deviceIdArr = new byte[] {0,0x58};
		
		String oldPassword1 = "6R3SW6H8";
		String newPassword1 = "11111111";
		String oldPassword2 = "77552257";
		String oldPassword3 = "33992258";
		
		byte[] data = new byte[16];
		byte[] oldPasswordArr1 = oldPassword1.getBytes();
		byte[] newPasswordArr1 = newPassword1.getBytes();
		System.arraycopy(oldPasswordArr1, 0, data, 0, 8);
		System.arraycopy(newPasswordArr1, 0, data, 8, 8);
		
		byte[] command = personPositionMessageFactory.createMessage(deviceIdArr, (byte)0x4D, data);
		System.out.println(ByteUtil.byteArrToHexString(command,true));
	}
}
