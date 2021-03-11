package com.bimuo.easy.collection.personposition;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bimuo.easy.collection.personposition.core.server.CollectionServer;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.PersonPositionMessageHandleFactory;

@SpringBootApplication
public class EasyCollectionPersonpositionApplication {
	@Autowired
	private PersonPositionMessageHandleFactory personPositionMessageHandleFactory;
	
	@Value("${hardware.connect.port}")
	private int serverPort; // 服务器端口号
	
	public static void main(String[] args) {
		SpringApplication.run(EasyCollectionPersonpositionApplication.class, args);
	}
	
	@PostConstruct
	public void init(){
		CollectionServer cs = new CollectionServer();
		cs.setServerName("personposition");
		cs.setServerPort(serverPort);
		cs.setMessageHandleFactory(personPositionMessageHandleFactory);
		cs.startAsync();
	}
}
