package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag30Vo;

/**
 * 将设备指令解码成标签类型30
 * 
 * @author Pingfan
 *
 */
public class Tag30Decoder implements ITagDecoder<Tag30Vo> {
	// 打印日志
	private final static Logger log = LogManager.getLogger(Tag30Decoder.class);

	@Override
	public List<Tag30Vo> decoder(PersonPositionMessage msg) {
		List<Tag30Vo> tags = new ArrayList<>();
		
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签30个数为:{}", tagCount);
		
		// Data字段标签,含有多个相同标签,i表示字节
		for(int i = 1; i< msg.getData().length; i+=7) {
			// 标签30类型,需要解tagId,activate,voltage,tamper,button1,button2,gain,traverse,activatorId,RSSI,各字段均以位为单位，所以使用移位运算符
			Tag30Vo tag30 = new Tag30Vo();
			byte[] arrayTag = msg.getData();
			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			Long replaceArr = Long.valueOf(replaceStr, 2);
			String bTag = Long.toBinaryString(replaceArr);
			log.info("标签1的二进制为:{}(前两位保留位为0则不显示)",bTag);
			// 解tagId,3字节
			long rTagId = replaceArr >>> 32;
			
			// 解activate-button2,各1位
			//TODO 需要先&再移位,不然值会发生改变,下述需要修改,参考表标签10
			// 经试验,除首位和末位直接移位外,中间位需要先&再移位,同时写值会改变
			int rActivate = arrayTag[3] >>> 7;
			
			int andVoltage = arrayTag[3] & 0x7F;
			int rVoltage = andVoltage >>> 7;
			
			int andTamper = arrayTag[3] & 0x3F; 
			int rTamper = andTamper >>> 6;
			
			int andButton1 = arrayTag[3] & 0x1F;
			int rButton1 = andButton1 >>> 5;
			
			int andButton2 = arrayTag[3] & 0xF;
			int rButton2 =  andButton2 >>> 4;
			
			// 解gain,2位
			int andGain = arrayTag[3] & 0x7;
			int rGain =  andGain >>> 1;
			
			// 解traverse,1位
			int rTraverse = arrayTag[3] & 0x1;
			
			// 给tag30赋值
			tag30.setTagId(rTagId);
			
			if(rActivate==0) {
				tag30.setActivate("在不激活区");
			} else if(rVoltage == 1){
				tag30.setActivate("在激活区");
			} else {
				tag30.setActivate("解码数据异常,未读取到激活位置");
			}
			
			if(rVoltage == 0) {
				tag30.setVoltage("电压正常");
			} else if(rVoltage == 1){
				tag30.setVoltage("电压低");
			} else {
				tag30.setVoltage("解码数据异常,未读取到电压");
			}
			
			if(rTamper == 0) {
				tag30.setTamper("防拆正常");
			} else if(rTamper == 1){
				tag30.setTamper("防拆异常");
			} else {
				tag30.setTamper("解码数据异常,未读取到防拆状态");
			}
			
			if(rButton1 == 0) {
				tag30.setButton1("按钮1按下");
			} else if(rButton1 == 1){
				tag30.setButton1("按钮1未按下");
			} else {
				tag30.setButton1("解码数据异常,未读取到按钮1状态");
			}
			
			if(rButton2 == 0) {
				tag30.setButton2("按钮2按下");
			} else if(rButton2 == 1){
				tag30.setButton2("按钮2未按下");
			} else {
				tag30.setButton2("解码数据异常,未读取到按钮2状态");
			}
			
			String gain = ByteUtil.intToBinary(rGain);
			
			if(gain.equals("00")) {
				tag30.setGain("增益0");
			} else if(gain.equals("01")) {
				tag30.setGain("增益1");
			} else if(gain.equals("10")) {
				tag30.setGain("增益2");
			} else if(gain.equals("11")) {
				tag30.setGain("增益3");
			}
			
			if(rTraverse == 0) {
				tag30.setTraverse("没发生穿越");
			} else if(rTraverse == 1){
				tag30.setTraverse("发生穿越");
			} else {
				tag30.setTraverse("解码数据异常,未读取到穿越状态");
			}
			
			// 解activatorId,2字节
			byte[] arrayActivatorId = new byte[2];
			System.arraycopy(arrayTag, 4, arrayActivatorId, 0, 2);
			int rActivatorId = ByteUtil.byteArrayToInt(arrayActivatorId);
			tag30.setActivatorId(rActivatorId);
			
			// 解RSSI,1字节
			byte[] arrayRSSI = new byte[1];
			System.arraycopy(arrayTag, 6, arrayActivatorId, 0, 1);
			int rRSSI = ByteUtil.byteArrayToInt(arrayRSSI);
			tag30.setRSSI(rRSSI);
			
			tags.add(tag30);
			log.info("第{}个标签30的tagId={},activate={},voltage={},tamper={},button1={},button2={},gain={},traverse={},activatorId={},RSSI={}",i,
					Long.toBinaryString(tag30.getTagId()),
					tag30.getActivate(),
					tag30.getVoltage(),
					tag30.getTamper(),
					tag30.getButton1(),
					tag30.getButton2(),
					tag30.getGain(),
					tag30.getTraverse(),
					Integer.toBinaryString(tag30.getActivatorId()),
					Integer.toBinaryString(tag30.getRSSI()));
		}
		return tags;
	}

}
