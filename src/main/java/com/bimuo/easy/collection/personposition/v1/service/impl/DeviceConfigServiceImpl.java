package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

@Transactional
@Service
public class DeviceConfigServiceImpl implements IDeviceConfigService {
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	
	Map<String, DeviceConfigReadVo> map = new HashMap<String, DeviceConfigReadVo>();

	@Override
	public DeviceConfigReadVo readConfig(String deviceId,String command) {
		// 1.采集向硬件发送指令,PersonPositionResponseHandleContext.channelActive()中实现
		DeviceConfigReadVo config = findByDeviceId(deviceId);
		// 2.等待硬件回复的消息,需要异步处理
		while(config != null) {
			
		}
		// 3.返回配置信息
		return config;
	}

	@Override
	public void addMemory(PersonPositionMessage msg, DeviceConfigReadVo dconfig) {
		map.put(ByteUtil.byteArrToHexString(msg.getDevId()), dconfig);
	}

	@Override
	public DeviceConfigReadVo findByDeviceId(String deviceId) {
		return map.get(deviceId);
	}
	


}
