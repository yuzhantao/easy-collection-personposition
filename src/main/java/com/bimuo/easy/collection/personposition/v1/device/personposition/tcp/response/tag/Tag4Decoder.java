package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.Tag4Vo;

/**
 * 将设备指令解码成标签类型4
 * 
 * @author Pingfan
 *
 */
public class Tag4Decoder implements ITagDecoder<Tag4Vo> {
	private final static Logger log = LogManager.getLogger(Tag0Decoder.class);
	
	@Override
	public List<Tag4Vo> decoder(PersonPositionMessage msg) {
		List<Tag4Vo> tags = new ArrayList<>();
		// Data字段标签个数
		byte tagCount = msg.getData()[0];
		log.info("该Data段的标签4个数为:{}",tagCount);
		// Data字段标签,含有多个相同标签,i表示字节
		for(int i = 1; i < msg.getData().length; i += 3) {
			// 标签4类型,3字节全部表示tagId,不含任何特征位
			Tag4Vo tag4 = new Tag4Vo();
			byte[] arrayTag = msg.getData();
			// 查看标签的二进制形式
			String replaceStr = new String(arrayTag);
			int rTagId = Integer.valueOf(replaceStr, 2);
			String bTag = ByteUtil.intToBinary(rTagId);
			log.info("标签4的二进制为:{}(前两位保留位为0则不显示)",bTag);
			// 解tagId,3字节
			tag4.setTagId(rTagId);
			tags.add(tag4);
			log.info("第{}个标签4的tagId={}",i,bTag);
		}
		return tags;
	}
}
