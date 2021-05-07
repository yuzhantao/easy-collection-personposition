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
 * 读取硬件全部配置
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class GetPersonPositionDeviceAllConfigTask {
	private final static Logger log = LogManager.getLogger(GetPersonPositionDeviceAllConfigTask.class);
	private PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	byte[] dataNetwork = {0x41};
	byte[] dataPort0 = {0x42};
	byte[] dataPort1 = {0x43};
	
	// 采集发送给硬件的指令,用来读取硬件总配置
	byte[] baseConfigDatas = {0x02,0x03,0x04,0x05,0x00,0x0B,0x00,0x58,0x44,0x00,(byte) 0xB5};  // 读取基本配置
	byte[] networkParamsDatas = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes("0058"), (byte) 0x47, dataNetwork);  // 读取网络参数
	byte[] port0ConfigDatas = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes("0058"), (byte) 0x47, dataPort0);  // 读取端口0
	byte[] port1ConfigDatas = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes("0058"), (byte) 0x47, dataPort1);  // 读取端口1
	
	int flag = 0; // 指令计数
	String cmdType = "";
	byte[] command = null;
	
	/**
	 * 定时给硬件发送指令,轮询间隔10s
	 */
	@Scheduled(cron = "0/10 * * * * ?")
	public void runfirst() {
		if(flag == 0) {
			command = baseConfigDatas;
			cmdType = "基础配置";
		}else if(flag == 1) {
			command = networkParamsDatas;
			cmdType = "网络参数";
		}else if(flag == 2) {
			command = port0ConfigDatas;
			cmdType = "端口0配置";
		}else if(flag == 3) {
			command = port1ConfigDatas;
			cmdType = "端口1配置";
		}
		ByteBuf bs = Unpooled.copiedBuffer(command);
		ChannelGroupFuture future = CollectionServer.channels.writeAndFlush(bs); // channels向所有设备发送指令
//		log.debug("********轮询开始********");
		// 监听发送状态
		future.addListener(new ChannelGroupFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelGroupFuture future) throws Exception {
				if (future.isSuccess()) {
					log.debug("轮询【{}】成功!", cmdType);
				} else {
					log.debug("轮询【{}】失败!", cmdType);
				}
			}
		});
		flag += 1;
		if(flag > 3) {
			flag = 0;
		}
	}
	
	
	
	
	
	/**
	 * 对一些资源进行操作的备用方法
	 */
	@PreDestroy
	public void destroy() {
		
	}
}
