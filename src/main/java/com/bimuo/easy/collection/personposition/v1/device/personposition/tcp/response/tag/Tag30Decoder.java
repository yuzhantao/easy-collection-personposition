package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag1Vo;
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
			
			int rActivate = arrayTag[3] >>> 7;
			int rVoltage = arrayTag[3] & 0x7F >>> 7;
			int rTamper = arrayTag[3] & 0x3F >>> 6;
			int rButton1 = arrayTag[3] & 0x1F >>> 5;
			int rButton2 = arrayTag[3] & 0xF >>> 4;
			// 解gain,2位
			int rGain = arrayTag[3] & 0x7 >>> 1;
			
			// 解traverse,1位
			int rTraverse = arrayTag[3] & 0x1;
			
			tag30.setTagId(rTagId);
			tag30.setActivate(rActivate==1);
			tag30.setVoltage(rVoltage==1);
			tag30.setTamper(rTamper==1);
			tag30.setButton1(rButton1==1);
			tag30.setButton2(rButton2==1);
			
			String gain = ByteUtil.intToBinary(rGain);
			if(gain.equals("00")) {
				tag30.setGain("0");
			} else if(gain.equals("01")) {
				tag30.setGain("1");
			} else if(gain.equals("10")) {
				tag30.setGain("2");
			} else if(gain.equals("11")) {
				tag30.setGain("3");
			}
			
			tag30.setTraverse(rTraverse==1);
			
			// 解activatorId,2字节
			byte[] arrayActivatorId = null;
			System.arraycopy(arrayTag, 4, arrayActivatorId, 0, 2);
			int rActivatorId = ByteUtil.byteArrayToInt(arrayActivatorId);
			tag30.setActivatorId(rActivatorId);
			
			// 解RSSI,1字节
			byte[] arrayRSSI = null;
			System.arraycopy(arrayTag, 6, arrayActivatorId, 0, 1);
			int rRSSI = ByteUtil.byteArrayToInt(arrayRSSI);
			tag30.setRSSI(rRSSI);
			
			tags.add(tag30);
			log.info("第{}个标签30的tagId={},activate={},voltage={},tamper={},button1={},button2={},gain={},traverse={},activatorId={},RSSI={}",i,
					Long.toBinaryString(rTagId),
					ByteUtil.intToBinary(rActivate),
					ByteUtil.intToBinary(rVoltage),
					ByteUtil.intToBinary(rTamper),
					ByteUtil.intToBinary(rButton1),
					ByteUtil.intToBinary(rButton2),
					ByteUtil.intToBinary(rGain),
					ByteUtil.intToBinary(rTraverse),
					ByteUtil.intToBinary(rActivatorId),
					ByteUtil.intToBinary(rRSSI));
		}
		return tags;
	}

}
