package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

/**
 * 移位运算测试3(模拟硬件数据字标签类型1解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag1 {

	public static void main(String[] args) {
		String str = "001011000000000000000001";
		byte[] src = str.getBytes();
		String res = new String(src);
		int replaceArr = Integer.valueOf(res, 2) & 0x3FFFFF;
		System.out.println("&前两位保留位后数据是:"+ByteUtil.intToBinary(replaceArr));
		int r1 = replaceArr >>> 20;
		System.out.println("gain字段2位(数据第3-4位):"+ByteUtil.intToBinary(r1));
		int r2 = replaceArr & 0xFFFFF;
		System.out.println("tagId字段20位(数据第5-24位):"+ByteUtil.intToBinary(r2));
		int oldR1 = r1<<20;
		int old = oldR1 + r2;
		System.out.println("完整的数据是:" + ByteUtil.intToBinary(old));
	}

}
