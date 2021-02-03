package com.bimuo.easy.collection.personposition.v1.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.mqtt.IMqttMessageSenderService;

/**
 * 消息测试控制器(用于接收http请求，模拟发送消息)
 * 
 * @author Pingfan
 *
 */
@RestController
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
@RequestMapping("/public/mqtt")
public class MqttController {
	private Logger logger = LogManager.getLogger(this.getClass());
    private final IMqttMessageSenderService mqttMessageSender;

    public MqttController(IMqttMessageSenderService mqttMessageSender) {
        this.mqttMessageSender = mqttMessageSender;
    }

    /**
     * 发送mqtt消息
     *
     * @param topic 消息主题
     * @param data  消息内容
     * @return
     */
    @PostMapping("/send")
    public void send(String topic, String data) {
    	topic = "personposition";
    	data = "hello";
        this.logger.info("开始发送mqtt消息,主题：{},消息：{}", topic, data);
        if (StringUtils.isNotBlank(topic)) {
            this.mqttMessageSender.sendToMqtt(topic, data);
        } else {
            this.mqttMessageSender.sendToMqtt(data);
        }
        this.logger.info("发送mqtt消息完成,主题：{},消息：{}", topic, data);
    }
}
