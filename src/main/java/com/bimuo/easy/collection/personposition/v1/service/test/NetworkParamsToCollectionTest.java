package com.bimuo.easy.collection.personposition.v1.service.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

public class NetworkParamsToCollectionTest {

	public static void main(String[] args) {
		byte[] sourceIp = {(byte) 192, (byte) 168, 1, (byte) 254};
		String sourceIpStr = new String();
		for(int i=0; i<sourceIp.length; i++) {
			String sourceIpArr1 = Integer.toString(ByteUtil.byteToInt(sourceIp[i]));
			String sourceIpStr2 = sourceIpArr1;
			sourceIpStr = sourceIpStr2 + ".";
		}
		System.out.println(sourceIpStr);
	}
}
