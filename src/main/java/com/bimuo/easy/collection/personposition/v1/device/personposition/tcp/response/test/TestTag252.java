package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 移位运算测试8(模拟硬件数据字标签类型252解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag252 {
	private final static Logger log = LogManager.getLogger(TestTag30.class);
	
	public static void main(String[] args) {
		// 模拟原数据
		int replaceArr = Integer.valueOf("101011110111101011111110", 2);
		// 模拟各属性
		// Button	Voltage	TagId(11)	Sign	Temp(10)
		int rButton = replaceArr >>> 23;
		int andVoltage = replaceArr & 0x7FFFFF;
		int rVoltage = andVoltage >>> 22;
		// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变
		// System.out.println(Integer.toBinaryString(rVoltage));
		int andTagId = replaceArr & 0x3FFFFF;
		int rTagId = andTagId >>> 11;
		// 当前一属性是多位注意跳级&(7-3-1-0循环)
		int andSign = replaceArr & 0x7FF;
		int rSign = andSign >>> 10;
		int rTemp = replaceArr & 0x3FF;
		// 使用更长的数据类型全1&,将无符号数转为有符号数
		long longTemp = rTemp & 0xFFFFFFFF;
		float realIntTemp = rTemp / 10f;
		float realTemp = longTemp / 10f;
		
		System.out.println("rTemp=" + Integer.toBinaryString(rTemp));
		System.out.println("longTemp=" + Long.toBinaryString(longTemp));
		System.out.println("realIntTemp=" + realIntTemp);

		log.info("button={},voltage={},tagId={},sign={},realLongtemp={}", 
				Integer.toBinaryString(rButton),
				Integer.toBinaryString(rVoltage),
				Integer.toBinaryString(rTagId),
				Integer.toBinaryString(rSign),
				realTemp
				);
	}

}
