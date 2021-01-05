package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag10Vo;

/**
 * 将设备指令解码成标签类型10(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag10Decoder implements ITagDecoder<Tag10Vo> {
	private final static Logger log = LogManager.getLogger(Tag10Decoder.class);

	@Override
	public List<Tag10Vo> decoder(PersonPositionMessage msg) {
		List<Tag10Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签10个数为:{}", tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for (int i = 1; i < msg.getData().length; i += 3) {
			// 标签10类型,需要解tagType,voltage,button,tamper,tagId,均以位为单位，所以使用移位运算符
			Tag10Vo tag10 = new Tag10Vo();
			byte[] arrayTag = msg.getData();
			
			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int replaceArr = Integer.valueOf(replaceStr, 2);
			String bTag = Integer.toBinaryString(replaceArr);
			log.info("标签10的二进制为:{}(前两位保留位为0则不显示)", bTag);
			
			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变,参考TestTag10
			
			// 解tagTape,1位
			int rTagType = replaceArr >>> 23;
			// 解voltage,1位
			int andVoltage = replaceArr & 0x7FFFFF ;
			int rVoltage = andVoltage >>> 22;
			// 解button,1位
			int andButton = replaceArr & 0x3FFFFF;
			int rButton = andButton >>> 21;
			// 解tamper,1位
			int andTamper = replaceArr & 0x1FFFFF;
			int rTamper = andTamper >>> 20;
			// 解tagId,20位
			int rTagId = replaceArr & 0xFFFFF;

			// 赋值给tag10
			if(rTagType == 0) {
				tag10.setRealTagType("普通标签");
			} else if(rTagType == 1){
				tag10.setRealTagType("温度标签");
			} else {
				tag10.setRealTagType("解码数据异常,未读取到标签类型");
			}
			
			if(rVoltage == 0) {
				tag10.setVoltage("电压正常");
			} else if(rVoltage == 1){
				tag10.setVoltage("电压低");
			} else {
				tag10.setVoltage("解码数据异常,未读取到电压");
			}
			
			if(rButton == 0) {
				tag10.setButton("按钮按下");
			} else if(rButton == 1){
				tag10.setButton("按钮未按下");
			} else {
				tag10.setButton("解码数据异常,未读取到按钮状态");
			}
			
			if(rTamper == 0) {
				tag10.setTamper("防拆正常");
			} else if(rTamper == 1){
				tag10.setTamper("防拆异常");
			} else {
				tag10.setTamper("解码数据异常,未读取到防拆状态");
			}
			
			tag10.setTagId(rTagId);
			tags.add(tag10);
			
			log.info("第{}个标签10的tagTape={},voltage={},button={},tamper={},tagId={}",
					i,
					tag10.getRealTagType(),
					tag10.getVoltage(),
					tag10.getButton(),
					tag10.getTamper(),
					Integer.toBinaryString(tag10.getTagId())				
					);		
		}

		return tags;
	}

}
