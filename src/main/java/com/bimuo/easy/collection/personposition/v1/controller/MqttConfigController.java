package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt-config")
public class MqttConfigController {
	@Value("${mqtt.ip}")
	private String mqttIp;
	@Value("${mqtt.websocket.port}")
	private String mqttPort;
	@Value("${mqtt.username}")
	private String mqttUser;
	@Value("${mqtt.pwd}")
	private String mqttPassword;
	
	  
    @GetMapping()
	public ResponseEntity<?> queryDevice() throws Exception{
		Map<String,Object> configMap = new HashMap<>();
		configMap.put("ip", mqttIp);
		configMap.put("port", mqttPort);
		configMap.put("user", mqttUser);
		configMap.put("password", mqttPassword);
		return ResponseEntity.ok(configMap);
	}
}
