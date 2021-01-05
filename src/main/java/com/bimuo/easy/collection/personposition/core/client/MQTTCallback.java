package com.bimuo.easy.collection.personposition.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTCallback implements MqttCallback {
	private static final Logger LOGGER = LogManager.getLogger(MQTTCallback.class);
	 
    private MQTTClient myMQTTClient;
 
    public MQTTCallback(MQTTClient client) {
        this.myMQTTClient = client;
    }
 
    /**
     * 丢失连接，可在这里做重连
     * 只会调用�?�?
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.error("连接断开，下面做重连...");
        long reconnectTimes = 1;
        while (this.myMQTTClient.isClose()==false) {
            try {
                if (this.myMQTTClient.getClient().isConnected()) {
                    LOGGER.warn("mqtt reconnect success end");
                    return;
                }
                LOGGER.warn("mqtt reconnect times = {} try again...", reconnectTimes++);
                this.myMQTTClient.getClient().reconnect();
            } catch (MqttException e) {
                LOGGER.error("", e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                //e1.printStackTrace();
            }
        }
    }
 
    /**
     * 消息到达�?
     * subscribe后，执行的回调函�?
     *
     * @param s
     * @param mqttMessage
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        // subscribe后得到的消息会执行到这里�?
        LOGGER.info("接收消息主题 : {}，接收消息内�? : {}", s, new String(mqttMessage.getPayload()));
    }
 
    /**
     * publish后，配�?�完成后回调的方�?
     *
     * @param iMqttDeliveryToken
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//        LOGGER.info("==========deliveryComplete={}==========", iMqttDeliveryToken.isComplete());
    }

}
