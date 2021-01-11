package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.message.IMessageHandleFactory;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.coder.PersonPositionDecoder;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.service.DeviceCodeService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;

import io.netty.channel.ChannelHandler;

@Service
public class PersonPositionMessageHandleFactory implements IMessageHandleFactory {
	@Autowired
	PersonPositionEventBusService personPositionEventBusService;
	
	@Autowired
	DeviceCodeService deviceCodeService;
	
	@Autowired
	IPersonPositionDeviceService personPositionDeviceService;
	
	@Override
	public ChannelHandler createMessageDecoder() {
		return new PersonPositionDecoder(Integer.MAX_VALUE, 6, 2, 2, 0, true);
	}

	@Override
	public ChannelHandler createMessageHandle() {
		return new PersonPositionResponseHandleContext(personPositionEventBusService,deviceCodeService,personPositionDeviceService);
	}

}
