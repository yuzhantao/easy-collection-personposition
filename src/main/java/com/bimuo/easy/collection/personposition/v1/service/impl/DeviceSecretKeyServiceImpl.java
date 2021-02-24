package com.bimuo.easy.collection.personposition.v1.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceSecretKeyFormatException;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceSecretKeyService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

@Service
public class DeviceSecretKeyServiceImpl implements IDeviceSecretKeyService {
	private static final Logger logger = LogManager.getLogger(DeviceSettingServiceImpl.class);

	@Override
	public void updateHardwareSecretKey(String deviceId, String oldPassword, String newPassword) {
		// Controller已判断参数全空,此处不需要
		// 发送给硬件更改密钥的指令,参考协议4D
		byte[] dataArr = new byte[16]; // data段16字节
		// 设置各参数
		if (StringUtils.isNotBlank(oldPassword) && oldPassword.length() == 8) {
			byte[] oldPasswordArr = oldPassword.getBytes();
			System.arraycopy(oldPasswordArr, 0, dataArr, 0, 8); // 将转换后的byte数组赋值给指令对应位置
		} else {
			new DeviceSecretKeyFormatException();
		}
		if (StringUtils.isNotBlank(newPassword) && newPassword.length() == 8) {
			byte[] newPasswordArr = newPassword.getBytes();
			System.arraycopy(newPasswordArr, 0, dataArr, 8, 8);
		} else {
			new DeviceSecretKeyFormatException();
		}

		byte[] command = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes(deviceId), (byte) 0x4D, dataArr);
		logger.info("向硬件发送的更改密钥指令是{}", ByteUtil.byteArrToHexString(command, true));
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(deviceId);
		if (channel == null) {
			logger.error("code-channel表中不存在设备编号【{}】的管道或该管道已被系统删除", deviceId);
		} else if (!channel.isActive() || !channel.isWritable()) {
			logger.info("设备编号【{}】的管道不可用", deviceId);
		} else {
			logger.info("设备编号【{}】对应的管道是{}", deviceId, channel);
			ByteBuf bs = Unpooled.copiedBuffer(command);
			ChannelFuture cf = channel.writeAndFlush(bs);
			// 回调函数监听是否发送成功
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						logger.info("发送更改密钥命令成功,下发命令={}", ByteUtil.byteArrToHexString(command, true));
					} else {
						logger.error("发送更改密钥命令失败,下发命令={}", ByteUtil.byteArrToHexString(command, true));
					}
				}
			});
		}

	}

}
