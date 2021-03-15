//package com.bimuo.easy.collection.personposition.v1.task;
//
//import java.util.List;
//
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
//
///**
// * 将连接但不回复消息的设备定义为离线
// * 
// * @author Pingfan
// *
// */
//@Component
//@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
//public class DeviceStateCheckTask {
//
//	/**
//	 * 定时查询硬件状态,轮询间隔5s
//	 */
//	@Scheduled(cron = "0/5 * * * * ?")
//    public void runfirst() {
//	List<String> codeList = CodeMapping.getInstance().findAllCode();
//	for(int i=0; i<codeList.size(); i++) {
//		System.out.println(codeList.get(i));
//	}
//	}
//	
//}
