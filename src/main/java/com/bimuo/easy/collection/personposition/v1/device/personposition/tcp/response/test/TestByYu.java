package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

/**
 * 移位运算测试(测试移位)
 * 
 * @author yuzhantao
 *
 */
public class TestByYu {

	public static void main(String[] args) {
		byte src = Integer.valueOf("11100000", 2).byteValue();
		// 与运算去头两位
		int ddd =  src & 0x3F;
		System.out.println(ByteUtil.intToBinary(ddd));
		// 右移4位
		ddd>>>=4;
		System.out.println(ByteUtil.intToBinary(ddd));
		System.out.println(ddd);
		System.out.println("src="+ByteUtil.intToBinary(src));
	}

}
