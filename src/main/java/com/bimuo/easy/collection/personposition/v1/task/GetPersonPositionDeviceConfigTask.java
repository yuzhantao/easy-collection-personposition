package com.bimuo.easy.collection.personposition.v1.task;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.server.CollectionServer;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;

/**
 * 读取硬件配置
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class GetPersonPositionDeviceConfigTask {
	private final static Logger log = LogManager.getLogger(GetPersonPositionDeviceConfigTask.class);
	
	// 采集发送给硬件的指令,用来读取硬件配置
	byte[] datas = {0x02,0x03,0x04,0x05,0x00,0x0B,0x00,0x58,0x44,0x00,(byte) 0xB5};
	
	/**
	 * 定时给硬件发送指令,轮询间隔10s
	 */
	@Scheduled(cron = "0/10 * * * * ?")
    public void runfirst() {
		ByteBuf bs = Unpooled.copiedBuffer(datas);
		ChannelGroupFuture future = CollectionServer.channels.writeAndFlush(bs); // channels向所有设备发送指令
        log.info("********轮询开始********");
        // 监听发送状态
        future.addListener(new ChannelGroupFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelGroupFuture future) throws Exception {
				if (future.isSuccess()) {
					log.info("发送读取设备配置命令成功,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
				} else {
					log.error("发送读取设备配置命令失败,下发命令={}",ByteUtil.byteArrToHexString(datas, true));
				}
			}
		});
    }
	
	/**
	 * 对一些资源进行操作的备用方法
	 */
	@PreDestroy
	public void destroy() {
		
	}
}
