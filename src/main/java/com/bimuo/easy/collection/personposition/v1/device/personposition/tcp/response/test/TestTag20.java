package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 移位运算测试7(模拟硬件数据字标签类型20解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag20 {
	private final static Logger log = LogManager.getLogger(TestTag30.class);
	
	public static void main(String[] args) {
		// 模拟原数据
		int replaceArr = Integer.valueOf("101011110111111111111110", 2);
		// 模拟各属性
		// TagType	Voltage	Tamper	Button1	Button2	Gain(2)	TagId(17)
		int rTagType = replaceArr >>> 23;
		int andVoltage = replaceArr & 0x7FFFFF;
		int rVoltage = andVoltage >>> 22;
		// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变
		// System.out.println(Integer.toBinaryString(rVoltage));
		int andTamper = replaceArr & 0x3FFFFF;
		int rTamper = andTamper >>> 21;
		int andButton1 = replaceArr & 0x1FFFFF;
		int rButton1 = andButton1 >>> 20;
		int andButton2 = replaceArr & 0xFFFFF;
		int rButton2 = andButton2 >>> 19;
		int andGain = replaceArr & 0x7FFFF;
		int rGain = andGain >>> 17;
		// 当前一属性是多位注意跳级&(7-3-1-0循环)
		int rTagId = replaceArr & 0x1FFFF;

		log.info("tagType={},voltage={},tamper={},button1={},button2={},gain={},tagId={}", 
				Integer.toBinaryString(rTagType),
				Integer.toBinaryString(rVoltage), 
				Integer.toBinaryString(rTamper),
				Integer.toBinaryString(rButton1), 
				Integer.toBinaryString(rButton2),
				Integer.toBinaryString(rGain),
				Integer.toBinaryString(rTagId));
	}

}
