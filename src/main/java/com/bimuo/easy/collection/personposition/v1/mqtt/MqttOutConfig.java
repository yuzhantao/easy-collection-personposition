package com.bimuo.easy.collection.personposition.v1.mqtt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * mqtt出站消息处理配置
 * 
 * @author Pingfan
 *
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttOutConfig {

    /**
     * mqtt消息出站通道，用于发送出站消息
     *
     * @return
     */
    @Bean
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt消息出站通道默认配置，用于向外发出mqtt消息，当前案例中发到了同一个mqtt服务器
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        String clientId = "h-backend-mqtt-out-" + System.currentTimeMillis();
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId, factory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(2);
        messageHandler.setDefaultTopic("/crane/mqtt/demo/foo");
        return messageHandler;
    }
}