package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag253Vo;

/**
 * 将设备指令解码成标签类型253(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag253Decoder implements ITagDecoder<Tag253Vo> {
	private final static Logger log = LogManager.getLogger(Tag253Decoder.class);
	
	@Override
	public List<Tag253Vo> decoder(PersonPositionMessage msg) {
		List<Tag253Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签253个数为:{}", tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for (int i = 1; i < msg.getData().length; i += 3) {
			// 标签253类型,需要解tagType,voltage,tagId,sign,temp均以位为单位，所以使用移位运算符
			Tag253Vo tag253 = new Tag253Vo();
			byte[] arrayTag = msg.getData();

			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int replaceArr = Integer.valueOf(replaceStr, 2);
			String bTag = Integer.toBinaryString(replaceArr);
			log.info("标签253的二进制为:{}(前两位保留位为0则不显示)", bTag);

			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变,参考TestTag252

			// 解tagType,1位
			int rTagType = replaceArr >>> 23;
			// 解voltage,1位
			int andVoltage = replaceArr & 0x7FFFFF;
			int rVoltage = andVoltage >>> 22;
			// 解tagId,11位
			int andTagId = replaceArr & 0x3FFFFF;
			int rTagId = andTagId >>> 11;
			// 当前一属性是多位注意跳级&(7-3-1-0循环)
			// 解sign,1位
			int andSign = replaceArr & 0x7FF;
			int rSign = andSign >>> 10;
			// 解temp,10位
			int rTemp = replaceArr & 0x3FF;
			// 无符号数转为有符号数,使用更长的数据类型全1&
			long longTemp = rTemp & 0xFFFFFFFF;
			// 温度值字段表示无符号整数。实际温度=Temp/10，故实际湿度精确到0.1摄氏度
			float realTemp = longTemp / 10f;

			// 赋值给tag253
			if (rTagType == 0) {
				tag253.setRealTagType("普通标签");
			} else if (rTagType == 1) {
				tag253.setRealTagType("温度标签");
			} else {
				tag253.setRealTagType("解码数据异常,未读取到标签类型");
			}

			if (rVoltage == 0) {
				tag253.setVoltage("电压正常");
			} else if (rVoltage == 1) {
				tag253.setVoltage("电压低");
			} else {
				tag253.setVoltage("解码数据异常,未读取到电压");
			}

			tag253.setTagId(rTagId);

			if (rSign == 0) {
				tag253.setSign("温度值为零上");
			} else if (rSign == 1) {
				tag253.setSign("温度值为零下");
			} else {
				tag253.setSign("解码数据异常,未读取到温度区间");
			}

			tag253.setTemp(realTemp);

			tags.add(tag253);

			log.info("第{}个标签253的tagType={},voltage={},tagId={},sign={},temp={}",
					i,
					tag253.getRealTagType(),
					tag253.getVoltage(),
					Integer.toBinaryString(tag253.getTagId()),
					tag253.getSign(),
					tag253.getTemp());
		}
		return tags;
	}
}
