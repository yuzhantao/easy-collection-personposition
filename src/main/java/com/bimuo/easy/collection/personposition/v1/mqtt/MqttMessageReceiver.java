package com.bimuo.easy.collection.personposition.v1.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * mqtt订阅消息处理
 * 
 * @author Pingfan
 *
 */
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttMessageReceiver implements MessageHandler {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
        String payload = String.valueOf(message.getPayload());
        logger.info("接收到 mqtt消息，主题:{} 消息:{}", topic, payload);
    }
}