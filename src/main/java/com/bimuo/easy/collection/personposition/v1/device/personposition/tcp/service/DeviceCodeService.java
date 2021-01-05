package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

/**
 * 解析电脑发出读取设备配置命令后设备回复的指令
 * 
 * @author Pingfan
 *
 */
@Service
public class DeviceCodeService {
	
	Map<String, DeviceConfigReadVo> map = new HashMap<String, DeviceConfigReadVo>();
	
	public DeviceCodeService() {
		super();
	}

	// 将设备回复的指令写入内存
	public void add(PersonPositionMessage msg, DeviceConfigReadVo dconfig) {
		map.put(ByteUtil.byteArrToHexString(msg.getDevId()), dconfig);
	}
	
	// 根据设备编号查询回复的指令
	public DeviceConfigReadVo findByDeviceId(String deviceId) {
		return map.get(deviceId);
	}
	
}
