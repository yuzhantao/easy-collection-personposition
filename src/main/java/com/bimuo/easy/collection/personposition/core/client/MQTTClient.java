package com.bimuo.easy.collection.personposition.core.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.bimuo.easy.collection.personposition.v1.service.PersonPositionEventBusService;
import com.bimuo.easy.collection.personposition.v1.service.vo.MqttMessageVo;

public class MQTTClient implements MqttCallback, MqttCallbackExtended {
	private static final Logger LOGGER = LogManager.getLogger(MQTTClient.class);

	private MqttClient client;
	private boolean isClose = false;
	private PersonPositionEventBusService personPositionEventBusService;

	public boolean isClose() {
		return isClose;
	}

	public void setClose(boolean isClose) {
		this.isClose = isClose;
	}

	public MqttClient getClient() {
		return client;
	}

	public void setClient(MqttClient client) {
		this.client = client;
	}

	private String host;
	private String username;
	private String password;
	private String clientId;
	private int timeout;
	private int keepalive;
	private String topic;
	private boolean isConsume;

	public MQTTClient(PersonPositionEventBusService personPositionEventBusService, String host, String username, String password,
			String clientId, String topic, int timeOut, int keepAlive, boolean isConsume) {
		this.personPositionEventBusService = personPositionEventBusService;
		this.topic = topic;
		this.host = host;
		this.username = username;
		this.password = password;
		this.clientId = clientId;
		this.timeout = timeOut;
		this.keepalive = keepAlive;
		this.isConsume = isConsume;
	}

	/**
	 * 设置mqtt连接参数
	 *
	 * @param username
	 * @param password
	 * @param timeout
	 * @param keepalive
	 * @return
	 */
	public MqttConnectOptions setMqttConnectOptions(String username, String password, int timeout, int keepalive) {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		options.setConnectionTimeout(timeout);
		options.setKeepAliveInterval(keepalive);
		options.setCleanSession(false);
//		options.setAutomaticReconnect(true);
		return options;
	}

	/**
	 * 连接mqtt服务端，得到MqttClient连接对象
	 */
	public void connect() throws MqttException {
		if (client == null) {
			client = new MqttClient(host, clientId, new MemoryPersistence());
			client.setCallback(this);
		}

		MqttConnectOptions mqttConnectOptions = setMqttConnectOptions(username, password, timeout, keepalive);
		if (!client.isConnected()) {
			client.connect(mqttConnectOptions);
		} else {
			client.disconnect();
			client.connect(mqttConnectOptions);
		}
		this.isClose = false;
		LOGGER.info("MQTT connect success");// 未发生异常，则连接成功
	}

	public void disconnect() throws MqttException {
		this.isClose = true;
		if (client == null) {
			client.disconnect();
		}
	}

	/**
	 * 发布，默认qos�?0，非持久�?
	 *
	 * @param pushMessage
	 * @param topic
	 */
	public void publish(String pushMessage, String topic) {
		publish(pushMessage, topic, 0, false);
	}

	/**
	 * 发布消息
	 *
	 * @param pushMessage
	 * @param topic
	 * @param qos
	 * @param retained:留存
	 */
	public void publish(String pushMessage, String topic, int qos, boolean retained) {
		MqttMessage message = new MqttMessage();
		message.setPayload(pushMessage.getBytes());
		message.setQos(qos);
		message.setRetained(retained);
		MqttTopic mqttTopic = this.getClient().getTopic(topic);
		if (null == mqttTopic) {
			LOGGER.error("topic is not exist");
		}
		MqttDeliveryToken token;// Delivery:配�??
		synchronized (this) {// 注意：这里一定要同步，否则，在多线程publish的情况下，线程会发生死锁，分析见文章�?后补�?
			try {
				token = mqttTopic.publish(message);// 也是发�?�到执行队列中，等待执行线程执行，将消息发�?�到消息中间�?
				token.waitForCompletion(1000L);
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 订阅某个主题，qos默认�?0
	 *
	 * @param topic
	 */
	public void subscribe(String topic) {
		subscribe(topic, 0);
	}

	/**
	 * 订阅某个主题
	 *
	 * @param topic
	 * @param qos
	 */
	public void subscribe(String topic, int qos) {
		try {
			this.getClient().subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.error("连接断开，下面做重连...");
		long reconnectTimes = 1;
		while (this.isClose() == false) {
			try {
				if (this.client.isConnected()) {
					LOGGER.warn("mqtt reconnect success end");
					return;
				}
				LOGGER.warn("mqtt reconnect times = {} try again...", reconnectTimes++);
				this.client.reconnect();
			} catch (MqttException e) {
				LOGGER.error("", e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// e1.printStackTrace();
			}
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.info("接收消息主题 : {}，接收消息内容 : {}", topic, new String(message.getPayload()));
		MqttMessageVo mm = new MqttMessageVo();
		mm.setClientId(topic);
		mm.setHost(this.host);
		mm.setTopic(topic);
		mm.setPayload(message);
		personPositionEventBusService.post(mm);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		try {
			if (this.isConsume) {
				client.subscribe(this.topic, 1);
			}
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}
