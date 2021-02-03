package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.message.IMessageHandleFactory;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.coder.PersonPositionDecoder;
import com.bimuo.easy.collection.personposition.v1.mqtt.IMqttMessageSenderService;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;

import io.netty.channel.ChannelHandler;

@Service
public class PersonPositionMessageHandleFactory implements IMessageHandleFactory {
	@Autowired
	PersonPositionEventBusService personPositionEventBusService;
	
	@Autowired
	IDeviceConfigService deviceCodeService;
	
	@Autowired
	IPersonPositionDeviceService personPositionDeviceService;
	
	@Autowired
	ITagHistoryService tagHistoryService;
	
	@Autowired
	IMqttMessageSenderService mqttMessageSenderService;
	
	@Override
	public ChannelHandler createMessageDecoder() {
		return new PersonPositionDecoder(Integer.MAX_VALUE, 6, 2, 2, 0, true);
	}

	@Override
	public ChannelHandler createMessageHandle() {
		return new PersonPositionResponseHandleContext(personPositionEventBusService,deviceCodeService,personPositionDeviceService,tagHistoryService,mqttMessageSenderService);
	}

}
