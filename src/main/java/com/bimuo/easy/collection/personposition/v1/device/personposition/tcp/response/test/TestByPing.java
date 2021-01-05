package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

/**
 * 移位运算测试2(测试和)
 * 
 * @author Pingfan
 *
 */
public class TestByPing {

	public static void main(String[] args) {
		byte src = Integer.valueOf("11100000", 2).byteValue();
		int ddd =  src & 0x3F;
		System.out.println("原始ddd="+ByteUtil.intToBinary(ddd));
		int v0 = ddd >>> 2;
		System.out.println("ddd右移2位="+ByteUtil.intToBinary(ddd));
		int v1 = ddd >>> 3;
		System.out.println("ddd右移3位="+ByteUtil.intToBinary(ddd));
		int v2 = ddd >>> 4;
		System.out.println("ddd右移4位="+ByteUtil.intToBinary(ddd));
		int bTag = v0+v1+v2;
		System.out.println("v0=" + ByteUtil.intToBinary(v0));
		System.out.println("v1=" + ByteUtil.intToBinary(v1));
		System.out.println("v2=" + ByteUtil.intToBinary(v2));
		System.out.println("bTag=" + ByteUtil.intToBinary(bTag));
	}

}
