package com.bimuo.easy.collection.personposition.v1;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bimuo.easy.collection.personposition.core.message.IMessageHandleFactory;
import com.bimuo.easy.collection.personposition.core.server.CollectionServer;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.PersonPositionMessageHandleFactory;

import io.netty.channel.ChannelHandler;

@SpringBootApplication
public class EasyCollectionPersonpositionApplication {
	@Autowired
	private PersonPositionMessageHandleFactory personPositionMessageHandleFactory;
	
	public static void main(String[] args) {
		SpringApplication.run(EasyCollectionPersonpositionApplication.class, args);
	}
	
	@PostConstruct
	public void init(){
		CollectionServer cs = new CollectionServer();
		cs.setServerName("personposition");
		cs.setServerPort(32500);
		cs.setMessageHandleFactory(personPositionMessageHandleFactory);
		cs.startAsync();
	}
}
