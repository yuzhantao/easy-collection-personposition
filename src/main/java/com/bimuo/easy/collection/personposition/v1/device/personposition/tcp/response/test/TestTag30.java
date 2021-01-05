package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

/**
 * 移位运算测试4(模拟硬件数据字标签类型30解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag30 {
	
	private final static Logger log = LogManager.getLogger(TestTag30.class);

	public static void decoderTest() {
		//String str = "10000000000000000000000110000011100000000000000110000001";
		//byte[] src = str.getBytes();
		// 模拟tagId
		int replaceArr = Integer.valueOf("100000000000000000000001", 2); 
		//long r1 = replaceArr >>> 32;
		
		// 模拟activate-traverse,取出一字节
		//String res2 = new String(ByteUtil.getByteArr(src,24,32));
		int replaceArr2 = Integer.valueOf("10000011", 2);
		//System.out.println("第四字节的数据是:"+res2);
		
		int r2 = replaceArr2 >>> 7;
		int r3 = replaceArr2 & 0x7F >>> 7;
		int r4 = replaceArr2 & 0x3F >>> 6;
		int r5 = replaceArr2 & 0x1F >>> 5;
		int r6 = replaceArr2 & 0xF >>> 4;
		int r7 = replaceArr2 & 0x7 >>> 1;
		int r8 = replaceArr2 & 0x1;
		
		// 模拟activatorId,取出两字节
		//String res3 = new String(ByteUtil.getByteArr(src,32,48));
		int replaceArr3 = Integer.valueOf("1000000000000001", 2);
		
		// 模拟RSSI,取出一字节
		//String res4 = new String(ByteUtil.getByteArr(src,48,56));
		int replaceArr4 = Integer.valueOf("10000001", 2);
		int test = replaceArr4 & 0x7F << 6;
	
		log.info("tagId={},activate={},voltage={},tamper={},button1={},button2={},gain={},traverse={},activatorId={},RSSI={}",
				
				ByteUtil.intToBinary(replaceArr),
				ByteUtil.intToBinary(r2),
				ByteUtil.intToBinary(r3),
				ByteUtil.intToBinary(r4),
				ByteUtil.intToBinary(r5),
				ByteUtil.intToBinary(r6),
				ByteUtil.intToBinary(r7),
				ByteUtil.intToBinary(r8),
				ByteUtil.intToBinary(replaceArr3),
				ByteUtil.intToBinary(replaceArr4));
		
		System.out.println("test=" + ByteUtil.intToBinary(test));
	}
	
	public static void main(String[] args) {
//		byte[] datas=0x00000000000;
//		data[2] & 0xffffff >>>5
		decoderTest();
		
	}

}
