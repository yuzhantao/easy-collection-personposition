package com.bimuo.easy.collection.personposition.v1.service.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

public class NetworkParamsToHardwareTest {
	
	public static void main(String[] args) {
		// 模拟Controller传的值
		String sourceIp = "192.168.1.254";
		String subnetMask = "255.255.255.0";
		String gatway = "192.168.1.1";
		String sourceHardware = "00:16:24:38:65:FF";

		// String转为byte[]
		byte[] bSourceIp = new byte[4];
		for(int i = 0; i<bSourceIp.length; i++) {
			bSourceIp[i] = ByteUtil.ipToBytesByInet(sourceIp)[i];
			System.out.println("ip第" + i + "位是" + ByteUtil.byteToInt(bSourceIp[i]));
		}
		
		byte[] bSubnetMask = new byte[4];
		for(int i = 0; i<bSubnetMask.length; i++) {
			bSubnetMask[i] = ByteUtil.ipToBytesByInet(subnetMask)[i];
			System.out.println("子网掩码第" + i + "位是" + ByteUtil.byteToInt(bSubnetMask[i]));
		}
		
		byte[] bGatway = new byte[4];
		for(int i = 0; i<bGatway.length; i++) {
			bGatway[i] = ByteUtil.ipToBytesByInet(gatway)[i];
			System.out.println("网关第" + i + "位是" + ByteUtil.byteToInt(bGatway[i]));
		}
		
		String[] sourceHardwareArr = sourceHardware.split("\\:");
		byte[] bSourceHardware = new byte[6];
		for(int i = 0; i<bSourceHardware.length; i++) {
			int value = Integer.parseInt(sourceHardwareArr[i], 16);
			bSourceHardware[i] = (byte) value;
			System.out.println("Mac地址第" + i + "位的十进制是" + ByteUtil.byteToInt(bSourceHardware[i]));
		}
		
		// 模拟发送的硬件指令
		byte[] data = new byte[19]; // 修改网络参数data段共19字节
		data[0] = 0x41; // INS为A(41)表示网络参数
	}
	
}
