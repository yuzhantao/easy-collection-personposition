package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 移位运算测试6(模拟硬件数据字标签类型11解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag11 {

	private final static Logger log = LogManager.getLogger(TestTag30.class);
	
	public static void main(String[] args) {
		// 模拟原数据
		int replaceArr = Integer.valueOf("101110111111111111111110", 2);
		// 模拟各属性
		int rVoltage = replaceArr >>> 23;
		int andButton = replaceArr & 0x7FFFFF ;
		int rButton = andButton >>> 22;
		//经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变
		//System.out.println(Integer.toBinaryString(rVoltage));
		int andGain = replaceArr & 0x3FFFFF;
		int rGain = andGain >>> 20;
		// 当前一属性是多位注意跳级&(7-3-1-0循环)
		int rTagId = replaceArr & 0xFFFFF;
		
		log.info("Voltage={},Button={},Gain={},TagId={}",
				Integer.toBinaryString(rVoltage),
				Integer.toBinaryString(rButton),
				Integer.toBinaryString(rGain),
				Integer.toBinaryString(rTagId)
				);

	}
}
