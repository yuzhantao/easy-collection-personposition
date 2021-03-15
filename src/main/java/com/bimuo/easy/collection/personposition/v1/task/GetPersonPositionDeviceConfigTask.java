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
 * 读取硬件配置
 * 
 * @author Pingfan
 *
 */
@Component
@EnableScheduling // 定时任务注解,可以在启动类上注解也可以在当前类
public class GetPersonPositionDeviceConfigTask {
	private final static Logger log = LogManager.getLogger(GetPersonPositionDeviceConfigTask.class);
	private PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	// 采集发送给硬件的指令,用来读取硬件总配置
	byte[] baseConfigDatas = {0x02,0x03,0x04,0x05,0x00,0x0B,0x00,0x58,0x44,0x00,(byte) 0xB5};  // 读取基本配置
	
//	/**
//	 * 合并字节数组
//	 * @param values 需要合并的字节数组
//	 * @return 合并后的字节数组
//	 */
//	private static byte[] byteMergerAll(byte[]... values) {
//		int length_byte = 0;
//		for (int i = 0; i < values.length; i++) {
//			length_byte += values[i].length;
//		}
//		byte[] all_byte = new byte[length_byte];
//		int countLength = 0;
//		for (int i = 0; i < values.length; i++) {
//		    byte[] b = values[i];
//		    System.arraycopy(b, 0, all_byte, countLength, b.length);
//		    countLength += b.length;
//		}
//		return all_byte;
//	}
//	
//	byte[] command = byteMergerAll(baseConfigDatas,networkParamsDatas,port0ConfigDatas,port1ConfigDatas,port2ConfigDatas,port3ConfigDatas);
	
	/**
	 * 定时给硬件发送指令,轮询间隔10s
	 */
	@Scheduled(cron = "0/10 * * * * ?")
    public void runfirst() {
		ByteBuf bs = Unpooled.copiedBuffer(baseConfigDatas);
		ChannelGroupFuture future = CollectionServer.channels.writeAndFlush(bs); // channels向所有设备发送指令
        log.debug("********轮询开始********");
        // 监听发送状态
        future.addListener(new ChannelGroupFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelGroupFuture future) throws Exception {
				if (future.isSuccess()) {
					log.debug("发送读取设备总配置命令成功,下发命令={}",ByteUtil.byteArrToHexString(baseConfigDatas, true));
				} else {
					log.error("发送读取设备总配置命令失败,下发命令={}",ByteUtil.byteArrToHexString(baseConfigDatas, true));
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
