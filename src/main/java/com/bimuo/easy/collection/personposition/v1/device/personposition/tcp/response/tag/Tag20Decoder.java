package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag20Vo;

/**
 * 将设备指令解码成标签类型20(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag20Decoder implements ITagDecoder<Tag20Vo> {
	private final static Logger log = LogManager.getLogger(Tag20Decoder.class);

	@Override
	public List<Tag20Vo> decoder(PersonPositionMessage msg) {
		List<Tag20Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签20个数为:{}", tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for (int i = 1; i < msg.getData().length; i += 3) {
			// 标签20类型,需要解tagType,voltage,tamper,button1,button2,gain,tagId,均以位为单位，所以使用移位运算符
			Tag20Vo tag20 = new Tag20Vo();
			byte[] arrayTag = msg.getData();

			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int replaceArr = Integer.valueOf(replaceStr, 2);
			String bTag = Integer.toBinaryString(replaceArr);
			log.info("标签20的二进制为:{}(前两位保留位为0则不显示)", bTag);

			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变,参考TestTag20

			// 解tagType,1位
			int rTagType = replaceArr >>> 23;
			// 解voltage,1位
			int andVoltage = replaceArr & 0x7FFFFF;
			int rVoltage = andVoltage >>> 22;
			// 解tamper,1位
			int andTamper = replaceArr & 0x3FFFFF;
			int rTamper = andTamper >>> 21;
			// 解button1,1位
			int andButton1 = replaceArr & 0x1FFFFF;
			int rButton1 = andButton1 >>> 20;
			// 解button2,1位
			int andButton2 = replaceArr & 0xFFFFF;
			int rButton2 = andButton2 >>> 19;
			// 解gain,2位
			int andGain = replaceArr & 0x7FFFF;
			int rGain = andGain >>> 17;

			// 当前一属性是多位注意跳级&(7-3-1-0循环)
			// 解tagId,17位
			int rTagId = replaceArr & 0x1FFFF;

			// 赋值给tag20
			if (rTagType == 0) {
				tag20.setRealTagType("普通标签");
			} else if (rTagType == 1) {
				tag20.setRealTagType("温度标签");
			} else {
				tag20.setRealTagType("解码数据异常,未读取到标签类型");
			}

			if (rVoltage == 0) {
				tag20.setVoltage("电压正常");
			} else if (rVoltage == 1) {
				tag20.setVoltage("电压低");
			} else {
				tag20.setVoltage("解码数据异常,未读取到电压");
			}

			if (rTamper == 0) {
				tag20.setTamper("防拆正常");
			} else if (rTamper == 1) {
				tag20.setTamper("防拆异常");
			} else {
				tag20.setTamper("解码数据异常,未读取到防拆状态");
			}
			
			if (rButton1 == 0) {
				tag20.setButton1("按钮按下");
			} else if (rButton1 == 1) {
				tag20.setButton1("按钮未按下");
			} else {
				tag20.setButton1("解码数据异常,未读取到按钮状态");
			}

			if (rButton2 == 0) {
				tag20.setButton2("按钮按下");
			} else if (rButton2 == 1) {
				tag20.setButton2("按钮未按下");
			} else {
				tag20.setButton2("解码数据异常,未读取到按钮状态");
			}
			
			String gain = Integer.toBinaryString(rGain);
			if(gain.equals("00")) {
				tag20.setGain("0");
			} else if(gain.equals("01")) {
				tag20.setGain("1");
			} else if(gain.equals("10")) {
				tag20.setGain("2");
			} else if(gain.equals("11")) {
				tag20.setGain("3");
			}
			
			tag20.setTagId(rTagId);
			tags.add(tag20);

			log.info("第{}个标签20的tagType={},voltage={},tamper={},button1={},button2={},gain={},tagId={}",
				i,
				tag20.getRealTagType(),
				tag20.getVoltage(),
				tag20.getTamper(),
				tag20.getButton1(),
				tag20.getButton2(),
				tag20.getGain(),
				Integer.toBinaryString(tag20.getTagId())				
			);	
		}
		return tags;
	}
}
