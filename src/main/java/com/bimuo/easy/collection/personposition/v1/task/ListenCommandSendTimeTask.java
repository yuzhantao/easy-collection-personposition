package com.bimuo.easy.collection.personposition.v1.task;

import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bimuo.easy.collection.personposition.v1.mqtt.IMqttMessageSenderService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeChannelMapping;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeTimeMapping;

/**
 * 监听指令下发任务(解决指令长时间下发监听不到)
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class ListenCommandSendTimeTask {
	private final static Logger log = LogManager.getLogger(ListenCommandSendTimeTask.class);

	@Autowired
	private IMqttMessageSenderService mqttMessageSenderService; // mqtt相关服务

	/**
	 * 定时轮询code-time映射,轮询间隔1s
	 */
	@Scheduled(cron = "0/1 * * * * ?")
	public void runfirst() {
		List<String> codeTimeList = CodeTimeMapping.getInstance().findAllCode(); // code-time表里的编号,下发成功则为空
		if (!codeTimeList.isEmpty()) {
			List<String> codeChannelList = CodeChannelMapping.getInstance().findAllCode(); // code-channel表里的编号,表示当前连接的全部设备
			for (int i = 0; i < codeChannelList.size(); i++) {
				String deviceCode = codeChannelList.get(i);
				boolean isTimeOut = CodeTimeMapping.getInstance().CodeTimeMappingContainsKey(deviceCode); // 判断code-time表中是否有当前连接的设备
				if (isTimeOut) {
					Long sendTime = CodeTimeMapping.getInstance().getDate(deviceCode); // 指令发送时间
					Long currentTime = System.currentTimeMillis(); // 当前轮询时间
					Long time = currentTime - sendTime;
					if (time > 5000) { // 时间差>5s,认为硬件没有响应,向MQ发送消息,以便前端显示提示框(经测试,网络最多延迟5min)
						log.info("{}已超时,请更换网络或重启设备后重试!",codeTimeList.toString());
						// 超时先删除映射
						CodeTimeMapping.getInstance().removeCodeTimeMapping(deviceCode);
						// 再发送超时到mqtt(表明向硬件发指令始终没有监听到下达成功)
						String topic = "personpositon/" + deviceCode + "/timeout";
						String mqttData = deviceCode + "发送指令超时";
						if (StringUtils.isNotBlank(topic)) {
							mqttMessageSenderService.sendToMqtt(topic, mqttData);
							log.info("【{}】发送mqtt消息完成,主题:{},消息:{}", deviceCode, topic, mqttData);
						} else {
							log.error("【{}】发送mqtt消息失败,主题:{},消息:{}", deviceCode, topic, mqttData);
						}
					} else { // 时间差<5s,继续等待硬件响应
						log.debug("继续等待【{}】响应……",deviceCode);
					}
				} else { // 未查到编号继续循环
					log.debug("time表未查到channel表的【{}】",deviceCode);
					continue;
				}
			}
		} else {
			log.debug("指令全部下达成功!time表没有数据!");
		}
	}

	/**
	 * 对一些资源进行操作的备用方法
	 */
	@PreDestroy
	public void destroy() {

	}
}
