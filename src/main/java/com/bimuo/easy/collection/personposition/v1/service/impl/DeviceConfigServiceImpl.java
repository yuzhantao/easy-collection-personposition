package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bimuo.easy.collection.personposition.core.annotation.NotProguard;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigDeviceIdException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

@Transactional
@Service
public class DeviceConfigServiceImpl implements IDeviceConfigService {
	protected final static Logger logger = LogManager.getLogger(DeviceConfigServiceImpl.class);
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	Map<String, DeviceConfigReadVo> map = new HashMap<String, DeviceConfigReadVo>();
	
	@Override
	public DeviceConfigReadVo readConfig(String deviceId) {
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
		logger.info("==========【查询】设备编号【{}】配置信息成功!",deviceId);
		return ppd.getDeviceConfig();
	}

	@Override
	public void addMemory(PersonPositionMessage msg, DeviceConfigReadVo dconfig) {
		map.put(ByteUtil.byteArrToHexString(msg.getDevId()), dconfig);
	}

	@Override
	public DeviceConfigReadVo findByDeviceId(String deviceId) {
		return map.get(deviceId);
	}

	@Override
	public DeviceConfigReadVo updateConfig(
			String oldDeviceId,
			String deviceId,
			Byte cain1,
			Byte cain2,
			Byte airBaudrate,
			Byte baudrate,
			String buzzType,
			String ioInput,
			Byte critical,
			Byte filterTagTime,
			Byte sendInterval,
			Byte tagType,
			String crcEn) {
		// 更新设备配置
		DeviceConfigReadVo config = new DeviceConfigReadVo();
		if (StringUtils.isNotBlank(deviceId)) {
			config.setDeviceId(deviceId.toUpperCase());
		} else {
			config.setDeviceId(oldDeviceId.toUpperCase());
		}
		
		if (cain1 != null) {
			config.setCain1(cain1);
		}
		if ((cain2!= null)) {
			config.setCain2(cain2);
		}
		if ((airBaudrate != null)) {
			config.setAirBaudrate(airBaudrate);
		}
		if ((baudrate!= null)) {
			config.setBaudrate(baudrate);
		}
		if (StringUtils.isNotBlank(buzzType)) {
			if (buzzType.equals("0")) {
				config.setBuzzType("关");
			} else if (buzzType.equals("1")) {
				config.setBuzzType("开");
			} else {
				config.setBuzzType("未读取到蜂鸣器状态");
			};
		}
		if (StringUtils.isNotBlank(ioInput)) {
			if (buzzType.equals("0")) {
				config.setIoInput("无地感");
			} else if (buzzType.equals("1")) {
				config.setIoInput("有地感");
			} else {
				config.setIoInput("未读取到地感值");
			};
		}
		if ((critical!= null)) {
			config.setCritical(critical);
		}
		if ((filterTagTime!= null)) {
			config.setFilterTagTime(filterTagTime);
		}
		if ((sendInterval != null)) {
			config.setSendInterval(sendInterval);
		}
		if ((tagType != null)) {
			config.setTagType(tagType);
		}
		if (StringUtils.isNotBlank(crcEn)) {
			if (buzzType.equals("0")) {
				config.setCrcEn("取消");
			} else if (buzzType.equals("1")) {
				config.setCrcEn("有效");
			} else {
				config.setCrcEn("未读取到设备CRC状态");
			};
		}
		
		PersonPositionDevice oldDevice = this.personPositionDeviceRepository.getOneByDeviceCode(oldDeviceId);
		PersonPositionDevice newDevice = new PersonPositionDevice();
		
		// 旧设备其它属性赋给新设备
		if(StringUtils.isNotBlank(deviceId)) {
			newDevice.setDeviceCode(deviceId.toUpperCase());
			newDevice.setCreateTime(new Date());
			newDevice.setUpdateTime(new Date());
			newDevice.setIp(oldDevice.getIp());
			newDevice.setDeviceType(oldDevice.getDeviceType());
			newDevice.setDeviceState(oldDevice.getDeviceState());
		}
		if(config != null) {
			newDevice.setDeviceConfig(config);
		}
		// 添加新设备的记录,旧编号数据库信息改为无效
		personPositionDeviceService.insert(newDevice);
		oldDevice.setDescription("无效");
		
		// 注意String存的是地址,用equals判断值是否相同
		if(newDevice.getDeviceCode().equals(newDevice.getDeviceConfig().getDeviceId())) {
			logger.info("==========【修改】设备编号【{}】配置信息成功!",newDevice.getDeviceCode());
		} else {
			logger.error("==========【修改】设备编号【{}】配置信息失败!设备编号【{}】与配置中编号【{}】不一致!",
					newDevice.getDeviceCode(),newDevice.getDeviceCode(),newDevice.getDeviceConfig().getDeviceId());
			new DeviceConfigDeviceIdException();
		}

		return config;
	}

	@Override
	public void updateHardwareConfig(
			String oldDeviceId,
			String deviceId,
			Byte cain1,
			Byte cain2,
			Byte airBaudrate,
			Byte baudrate,
			String buzzType,
			String ioInput,
			Byte critical,
			Byte filterTagTime,
			Byte sendInterval,
			Byte tagType,
			String crcEn) {
		
		// 发送给硬件修改配置的指令,参考协议43
		byte[] dataArr = new byte[14]; // data段14字节
		// 设置各参数
		System.arraycopy(ByteUtil.hexStringToBytes(deviceId), 0, dataArr, 0, 2); // deviceId 2字节
		dataArr[2] = cain1;
		dataArr[3] = cain2;
		dataArr[4] = airBaudrate;
		dataArr[5] = baudrate;
		if(StringUtils.isNotBlank(buzzType)) {
			dataArr[6] = Byte.parseByte(buzzType);
		}
		if(StringUtils.isNotBlank(ioInput)) {
			dataArr[7] = Byte.parseByte(ioInput);
		}
		dataArr[8] = critical;
		dataArr[9] = filterTagTime;
		dataArr[10] = sendInterval;
		dataArr[11] = tagType;
		if(StringUtils.isNotBlank(crcEn)) {
			dataArr[12] = Byte.parseByte(crcEn);
		}
		dataArr[13] = 0x00; // Reserver保留位 1字节
		
		// 拼接发送给硬件的指令command
		String dataStr = ByteUtil.byteArrToHexString(dataArr);
		byte data = ByteUtil.intToByte(ByteUtil.hexStringToInt(dataStr));
		byte[] command = {0x02,0x03,0x04,0x05,0x00,0x15,0x00,0x58,0x43,0x00,data,(byte) 0xB4};
		
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(oldDeviceId);
		ByteBuf bs = Unpooled.copiedBuffer(command);
		ChannelFuture cf = channel.writeAndFlush(bs);
		// 回调函数监听是否发送成功
		cf.addListener(new ChannelFutureListener() {
			@NotProguard
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					logger.info("发送修改设备配置命令成功,下发命令={}",ByteUtil.byteArrToHexString(command, true));
					// 更新code-channel映射表
					CodeMapping.getInstance().removeChannel(channel);
					CodeMapping.getInstance().removeChannelMapping(oldDeviceId);
					CodeMapping.getInstance().addChannel(channel);
					CodeMapping.getInstance().addChannelMapping(deviceId, channel);
				} else {
					logger.error("发送修改设备配置命令失败,下发命令={}",ByteUtil.byteArrToHexString(command, true));
				}
			}
		});
		
		
		
	}
}
