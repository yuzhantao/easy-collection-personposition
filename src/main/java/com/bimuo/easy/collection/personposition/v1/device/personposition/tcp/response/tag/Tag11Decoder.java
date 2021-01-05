package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag11Vo;

/**
 * 将设备指令解码成标签类型11(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag11Decoder implements ITagDecoder<Tag11Vo> {
	private final static Logger log = LogManager.getLogger(Tag11Decoder.class);
	
	@Override
	public List<Tag11Vo> decoder(PersonPositionMessage msg) {
		List<Tag11Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签11个数为:{}", tagCount);

		// Data字段标签,含有多个相同标签,i表示字节
		for (int i = 1; i < msg.getData().length; i += 3) {
			// 标签11类型,需要解voltage,button,gain,tagId,均以位为单位，所以使用移位运算符
			Tag11Vo tag11 = new Tag11Vo();
			byte[] arrayTag = msg.getData();

			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int replaceArr = Integer.valueOf(replaceStr, 2);
			String bTag = Integer.toBinaryString(replaceArr);
			log.info("标签11的二进制为:{}(前两位保留位为0则不显示)", bTag);

			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变,参考TestTag11

			// 解voltage,1位
			int rVoltage = replaceArr >>> 23;
			// 解button,1位
			int andButton = replaceArr & 0x7FFFFF;
			int rButton = andButton >>> 22;
			// 解gain,2位
			int andGain = replaceArr & 0x3FFFFF;
			int rGain = andGain >>> 20;
			// 解tagId,20位
			int rTagId = replaceArr & 0xFFFFF;

			// 赋值给tag11
			if (rVoltage == 0) {
				tag11.setVoltage("电压正常");
			} else if (rVoltage == 1) {
				tag11.setVoltage("电压低");
			} else {
				tag11.setVoltage("解码数据异常,未读取到电压");
			}

			if (rButton == 0) {
				tag11.setButton("按钮按下");
			} else if (rButton == 1) {
				tag11.setButton("按钮未按下");
			} else {
				tag11.setButton("解码数据异常,未读取到按钮状态");
			}

			String gain = Integer.toBinaryString(rGain);
			if(gain.equals("00")) {
				tag11.setGain("0");
			} else if(gain.equals("01")) {
				tag11.setGain("1");
			} else if(gain.equals("10")) {
				tag11.setGain("2");
			} else if(gain.equals("11")) {
				tag11.setGain("3");
			}

			tag11.setTagId(rTagId);
			tags.add(tag11);

			log.info("第{}个标签11的voltage={},button={},gain={},tagId={}",
					i,
					tag11.getVoltage(),
					tag11.getButton(),
					tag11.getGain(),
					Integer.toBinaryString(tag11.getTagId())
			);
		}
		return tags;
	}
}
