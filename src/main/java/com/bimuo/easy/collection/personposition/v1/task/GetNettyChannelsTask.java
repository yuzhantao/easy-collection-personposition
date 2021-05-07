//package com.bimuo.easy.collection.personposition.v1.task;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.bimuo.easy.collection.personposition.core.server.CollectionServer;
//import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
//
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.util.concurrent.GlobalEventExecutor;
//
///**
// * 读取硬件全部配置
// * 
// * @author Pingfan
// *
// */
//@Component
//@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
//public class GetNettyChannelsTask {
//	private final static Logger log = LogManager.getLogger(GetNettyChannelsTask.class);
//	
//	@Scheduled(cron = "0/30 * * * * ?")
//	public void runfirst() {
//		ChannelGroup channels = CollectionServer.channels;
//		log.info("服务连接的全部channel={}",channels);
//		log.info("缓存中的code-channel={}", CodeMapping.getInstance().getAll());
//	}
//}
