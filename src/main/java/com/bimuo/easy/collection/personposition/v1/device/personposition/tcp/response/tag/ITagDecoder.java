package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.tag;

import java.util.List;

import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo.DeviceTagReadVo;


/**
 * 标签解码接口
 * 
 * @author Pingfan
 *
 */
public interface ITagDecoder<T extends DeviceTagReadVo> {
	List<T> decoder(PersonPositionMessage msg);
}
