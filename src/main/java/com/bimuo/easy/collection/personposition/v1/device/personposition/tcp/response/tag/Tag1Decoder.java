package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag1Vo;

/**
 * 将设备指令解码成标签类型1
 * 
 * @author yuzhantao
 *
 */
public class Tag1Decoder implements ITagDecoder<Tag1Vo> {
	// 打印日志
	private final static Logger log = LogManager.getLogger(Tag1Decoder.class);
	
	@Override
	public List<Tag1Vo> decoder(PersonPositionMessage msg) {
		
		List<Tag1Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签1个数为:{}", tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for(int i = 1; i < msg.getData().length; i += 3) {
			// 标签1类型,需要解gain和tagId,因为二者以位为单位，所以使用移位运算符
			Tag1Vo tag1 = new Tag1Vo();
			byte[] arrayTag = msg.getData();
			//byte[] arrayTag = new byte[4]; //标签1长度为3字节,开辟一个4字节的内存单元存放
			// 查看标签的二进制形式
			//System.arraycopy(msg.getData(), i, arrayTag, 1, 3);
			String replaceStr = new String(arrayTag);
			int replaceArr =  Integer.valueOf(replaceStr, 2) & 0x3FFFFF; //0x3FFFFF用来过滤前两位保留位
			//String bTag = ByteUtil.intToBinary(ByteUtil.bytesToInt(arrayTag, 0));
			String bTag = ByteUtil.intToBinary(replaceArr);
			log.info("标签1的二进制为:{}(前两位保留位为0则不显示)",bTag);
			// 解gain
			int rGain = replaceArr >>> 20;
			String gain = ByteUtil.intToBinary(rGain);
			//String gain = bTag.substring(2, 3);
			if(gain.equals("00")) {
				tag1.setGain("增益0");
			} else if(gain.equals("01")) {
				tag1.setGain("增益1");
			} else if(gain.equals("10")) {
				tag1.setGain("增益2");
			} else if(gain.equals("11")) {
				tag1.setGain("增益3");
			}
			// 解tagId
			int rTagId = replaceArr & 0xFFFFF; // 0xFFFFF用来过滤前4位得到后20位tagId
			tag1.setTagId(rTagId);
			tags.add(tag1);
			log.info("第{}个标签1的gain值:{}，ID值:{}",i,
					Integer.toBinaryString(tag1.getTagId()),
					tag1.getGain());
		}
		return tags;
	}
}
