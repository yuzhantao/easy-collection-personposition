package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 移位运算测试5(模拟硬件数据字标签类型10解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag10 {

	private final static Logger log = LogManager.getLogger(TestTag30.class);
	
	public static void main(String[] args) {
		// 模拟原数据
		int replaceArr = Integer.valueOf("101011111111111111111110", 2);
		// 模拟各属性
		int rTagType = replaceArr >>> 23;
		int andVoltage = replaceArr & 0x7FFFFF ;
		int rVoltage = andVoltage >>> 22;
		//经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变
		//System.out.println(Integer.toBinaryString(rVoltage));
		int andButton = replaceArr & 0x3FFFFF;
		int rButton = andButton >>> 21;
		int andTamper = replaceArr & 0x1FFFFF;
		int rTamper = andTamper >>> 20;
		int rTagId = replaceArr & 0xFFFFF;
		
		log.info("TagType={},Voltage={},Button={},Tamper={},TagId={}",
				Integer.toBinaryString(rTagType),
				Integer.toBinaryString(rVoltage),
				Integer.toBinaryString(rButton),
				Integer.toBinaryString(rTamper),
				Integer.toBinaryString(rTagId)
				);
	}

}
