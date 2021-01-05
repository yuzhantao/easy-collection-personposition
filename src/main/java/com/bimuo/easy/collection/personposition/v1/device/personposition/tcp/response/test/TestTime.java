package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.test;

import java.util.Date;

/**
 * 移位运算时间测试
 * 
 * @author Pingfan
 *
 */
public class TestTime {

	public static void main(String[] args) {
		Date d1 = new Date();
		long t1 = d1.getTime();
		for(int i=1;i<=10000;i++) {
			TestTag30.decoderTest();
		}
		Date d2 = new Date();
		long t2 = d2.getTime();
		int time = (int) ((t2 - t1) / 1000); 
		System.out.println("执行一万次的时间是:"+time+"秒");
	}
}
