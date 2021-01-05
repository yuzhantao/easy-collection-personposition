package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 移位运算测试9(模拟硬件数据字标签类型251解码)
 * 
 * @author Pingfan
 *
 */
public class TestTag251 {
	private final static Logger log = LogManager.getLogger(TestTag30.class);

	public static void main(String[] args) {
		// 模拟原数据
		Long replaceArr = Long.valueOf("101011110111101011111110011011101101101011111110110011110111101", 2);
		// 模拟各属性
		// TagId(24) Voltage Reserver(7)(保留位,占位不解析) Hum(16) Temp(16)
		long rTagId = replaceArr >>> 40;

		// 模拟Voltage-Reserver,取出一字节
		int replaceArr2 = Integer.valueOf("01101110", 2);
		int rVoltage = replaceArr2 >>> 7;
		
		// 在计算机中，负数除了最高位为1以外，还采用补码的形式，所以在计算前，需要对补码进行还原。
		
		// 模拟Hum,取2字节,有符号整数
		int replaceArr3 = Integer.valueOf("0101101011111110", 2);
		int rHum = replaceArr3 & 0X7FFF;
		double realHum = 0d;
		// 首位符号位,判断正负
		if(replaceArr3 >>> 15 == 0) {
			realHum = rHum / 100d;
		} else if(replaceArr3 >>> 15 == 1){
			realHum = (0 - rHum / 100d);
		} else {
			log.info("温度值异常,读取到的湿度符号位为:{}不合法" ,replaceArr3 >>> 15);
		}

		// 模拟Temp,取2字节,有符号整数
		int replaceArr4 = Integer.valueOf("1100111101111010", 2);
		int rTemp = replaceArr4 & 0x7FFF;
		double realTemp = 0d;
		if(replaceArr4 >>> 15 == 0) {
			realTemp = rTemp/100d;
		} else if(replaceArr4 >>> 15 == 1){
			realTemp = (0 - rTemp / 100d);
		} else {
			log.info("湿度值异常,读取到的温度符号位为:{}不合法" ,replaceArr3 >>> 15);
		}
		
		log.info("tagId={},voltage={},hum={},temp={}", 
				Long.toBinaryString(rTagId),
				Integer.toBinaryString(rVoltage),
				realHum,
				realTemp);
	}
}
