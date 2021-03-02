package com.bimuo.easy.collection.personposition.v1.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;

/**
 * 实时数据每晚23点清一次数据库
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class DataBaseClearTask {
	private final static Logger log = LogManager.getLogger(GetPersonPositionDeviceConfigTask.class);
	
	@Autowired
	private ITagHistoryService tagHistoryService;
	
	/**
	 * 定时给硬件发送指令,轮询间隔每晚23点
	 */
	@Scheduled(cron ="${database.cleartime}")
    public void runfirst() {
		tagHistoryService.clearTable();
		log.info("数据库更新成功!只留两天内实时数据!");
    }
}
