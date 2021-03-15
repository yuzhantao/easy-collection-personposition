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
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;

/**
 * 读取端口1配置
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class GetPersonPositionDevicePort1ConfigTask {
	private final static Logger log = LogManager.getLogger(GetPersonPositionDevicePort1ConfigTask.class);
	private PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	byte[] data = {0x43}; // 端口1二级指令C,十六进制0x43
	byte[] port1ConfigDatas = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes("0058"), (byte) 0x47, data);  // 读取端口1
	//byte[] port1ConfigDatas = {0x02,0x03,0x04,0x05,0x00,0x0E,0x00,0x58,0x47,0x00,0x43,(byte) 0xFC}; // 读取端口1配置
	
	/**
	 * 定时给硬件发送指令,轮询间隔10s
	 */
	@Scheduled(cron = "0/100 * * * * ?")
    public void runfirst() {
		ByteBuf bs = Unpooled.copiedBuffer(port1ConfigDatas);
		ChannelGroupFuture future = CollectionServer.channels.writeAndFlush(bs); // channels向所有设备发送指令
        log.debug("********轮询开始********");
        // 监听发送状态
        future.addListener(new ChannelGroupFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelGroupFuture future) throws Exception {
				if (future.isSuccess()) {
					log.debug("发送读取设备总配置命令成功,下发命令={}",ByteUtil.byteArrToHexString(port1ConfigDatas, true));
				} else {
					log.error("发送读取设备总配置命令失败,下发命令={}",ByteUtil.byteArrToHexString(port1ConfigDatas, true));
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
