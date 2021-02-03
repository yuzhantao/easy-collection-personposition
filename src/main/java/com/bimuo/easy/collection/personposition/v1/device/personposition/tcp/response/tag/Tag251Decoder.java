package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag251Vo;

/**
 * 将设备指令解码成标签类型251(8字节)
 * 
 * @author Pingfan
 *
 */
public class Tag251Decoder implements ITagDecoder<Tag251Vo> {
	private final static Logger log = LogManager.getLogger(Tag251Decoder.class);
	
	@Override
	public List<Tag251Vo> decoder(PersonPositionMessage msg) {
		List<Tag251Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签251个数为:{}", tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for (int i = 1; i < msg.getData().length; i += 8) {
			// 标签251类型,需要解tagId,voltage,(不解保留位,但保留位占了7位),hum,temp均以位为单位，所以使用移位运算符
			Tag251Vo tag251 = new Tag251Vo();
			byte[] arrayTag = msg.getData();

			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int replaceArr = Integer.valueOf(replaceStr, 2);
			String bTag = Integer.toBinaryString(replaceArr);
			log.info("标签251的二进制为:{}(前两位保留位为0则不显示)", bTag);

			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变,参考TestTag251

			// 解tagId,24位
			long rTagId = replaceArr >>> 40;

			// 解voltage,1位,取出arrayTag第3字节赋值给新数组arrayVoltage,再取新数组第0位
			byte[] arrayVoltage = new byte[1];
			System.arraycopy(arrayTag, 3, arrayVoltage, 0, 1);
			int rVoltage = arrayVoltage[0];

			// 解hum,16位,有符号整数,直接读取使用即可
			byte[] arrayHum = new byte[16];
			// 复制数组需跳过7位保留位,取arrayTag第4字节
			System.arraycopy(arrayTag, 4, arrayHum, 0, 2);
			int replaceHum = ByteUtil.byteArrayToInt(arrayHum);
//			// 首位为符号位,&掉只保留后15位数据位
//			int rHum = replaceHum & 0X7FFF;
			double realHum = replaceHum / 100d;
//			// 首位符号位,判断正负
//			if (replaceHum >>> 15 == 0) {
//				realHum = rHum / 100d;
//			} else if (replaceHum >>> 15 == 1) {
//				realHum = (0 - rHum / 100d);
//			} else {
//				log.info("湿度值异常,读取到的湿度符号位为:{}不合法", replaceHum >>> 15);
//			}

			// 解temp,16位,有符号整数,直接读取使用即可
			byte[] arrayTemp = new byte[16];
			// 取arrayTag第5字节
			System.arraycopy(arrayTag, 5, arrayTemp, 0, 2);
			int replaceTemp = ByteUtil.byteArrayToInt(arrayTemp);
//			// 首位为符号位,&掉只保留后15位数据位
//			int rTemp = replaceTemp & 0X7FFF;
			double realTemp = replaceTemp / 100d;
//			// 首位符号位,判断正负
//			if (replaceTemp >>> 15 == 0) {
//				realTemp = rTemp / 100d;
//			} else if (replaceTemp >>> 15 == 1) {
//				realTemp = (0 - rTemp / 100d);
//			} else {
//				log.info("温度值异常,读取到的温度符号位为:{}不合法", replaceTemp >>> 15);
//			}

			// 赋值给tag251
			tag251.setTagId(rTagId);

			if (rVoltage == 0) {
				tag251.setVoltage("电压正常");
			} else if (rVoltage == 1) {
				tag251.setVoltage("电压低");
			} else {
				tag251.setVoltage("解码数据异常,未读取到电压");
			}

			tag251.setHum(realHum);
			tag251.setTemp(realTemp);
			
			tags.add(tag251);
			
			log.info("第{}个标签251的tagId={},voltage={},hum={},tamper={}", 
					i, 
					Long.toBinaryString(tag251.getTagId()),
					tag251.getVoltage(), 
					tag251.getHum(), 
					tag251.getTemp());
		}
		return tags;
	}
}