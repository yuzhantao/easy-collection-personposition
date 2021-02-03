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
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigDeviceIdException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;
import com.google.common.base.Preconditions;

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
	
	/**
	 * 更新数据库设备配置
	 * @param config 设备配置
	 * @param deviceId 修改的设备编号
	 * @param cain1 发送增益,范围0~3
	 * @param cain2 接收增益,范围0~31
	 * @param airBaudrate 空中波特率,范围0~2(0:250K, 1:1M, 2:2M)
	 * @param baudrate 串口波特率,范围0~6(4800~115200)
	 * @param buzzType 蜂鸣器状态,范围0~1(0:关,1:开)
	 * @param ioInput 地感值,范围0~1(0:无地感,1:有地感)
	 * @param critical 两秒内接收到的同一个ID的次数阀值,范围0~8
	 * @param filterTagTime 同一个ID的过滤时间,单位秒,范围0~250
	 * @param sendInterval 两个韦根数据的发送间隔,单位0.1秒,范围0~250
	 * @param tagType 标签类型,范围0~255
	 * @param crcEn 设备CRC状态,范围0~1(0:取消,1:有效)
	 */
	private void setDeviceProperty(
			DeviceConfigReadVo config,
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
		if (StringUtils.isNotBlank(deviceId)) {
			config.setDeviceId(deviceId.toUpperCase());
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
	}
	
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
		DeviceConfigReadVo config = new DeviceConfigReadVo();
		// 判断用于查询的编号是否为空
		Preconditions.checkArgument(StringUtils.isNotBlank(oldDeviceId),new DeviceCodeNoneException());
		PersonPositionDevice oldDevice = this.personPositionDeviceRepository.getOneByDeviceCode(oldDeviceId);
		if(StringUtils.isBlank(deviceId) || deviceId.equals(oldDeviceId)) { // 未修改编号
			// 更新设备配置
			setDeviceProperty(config,deviceId,cain1,cain2,airBaudrate,baudrate,
					buzzType,ioInput,critical,filterTagTime,sendInterval,tagType,crcEn);
			// 更新原先记录
			oldDevice.setDeviceConfig(config);
			personPositionDeviceService.modify(oldDevice);
			if(oldDevice.getDeviceCode().equals(oldDevice.getDeviceConfig().getDeviceId())) {
				logger.info("==========数据库【修改】设备编号【{}】配置信息成功!",oldDevice.getDeviceCode());
			} else {
				logger.error("==========数据库【修改】设备编号【{}】配置信息失败!设备编号【{}】与配置中编号【{}】不一致!",
						oldDevice.getDeviceCode(),
						oldDevice.getDeviceCode(),
						oldDevice.getDeviceConfig().getDeviceId());
				new DeviceConfigDeviceIdException();
			}
			return config;
			} else { // 修改编号
				oldDevice.setEffective(false); // 设置旧记录无效
				PersonPositionDevice newDevice = new PersonPositionDevice();
				// 旧设备其它属性赋给新设备
				newDevice.setDeviceCode(deviceId.toUpperCase());
				newDevice.setCreateTime(new Date());
				newDevice.setUpdateTime(new Date());
				newDevice.setIp(oldDevice.getIp());
				newDevice.setDeviceType(oldDevice.getDeviceType());
				newDevice.setDeviceState(oldDevice.getDeviceState());
				setDeviceProperty(config,deviceId,cain1,cain2,airBaudrate,baudrate,
						buzzType,ioInput,critical,filterTagTime,sendInterval,tagType,crcEn);
				newDevice.setDeviceConfig(config);
				newDevice.setEffective(true);
				// 添加新设备的记录
				personPositionDeviceService.insert(newDevice);
				// 注意String存的是地址,用equals判断值是否相同
				if(newDevice.getDeviceCode().equals(newDevice.getDeviceConfig().getDeviceId())) {
					logger.info("==========【修改】设备编号【{}】配置信息成功!",newDevice.getDeviceCode());
				} else {
					logger.error("==========【修改】设备编号【{}】配置信息失败!设备编号【{}】与配置中编号【{}】不一致!",
							newDevice.getDeviceCode(),
							newDevice.getDeviceCode(),
							newDevice.getDeviceConfig().getDeviceId());
					new DeviceConfigDeviceIdException();
				}
				return config;
		}
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
		// Controller已判断参数全空,此处不需要
		// 发送给硬件修改配置的指令,参考协议43
		byte[] dataArr = new byte[14]; // data段14字节
		// 设置各参数
		if(StringUtils.isNotBlank(deviceId)) {
			System.arraycopy(ByteUtil.hexStringToBytes(deviceId), 0, dataArr, 0, 2); // deviceId 2字节
		}
		if(cain1 != null) {
			dataArr[2] = cain1;
		}
		if(cain2 != null) {
			dataArr[3] = cain2;
		}
		if(airBaudrate != null) {
			dataArr[4] = airBaudrate;
		}
		if(baudrate != null) {
			dataArr[5] = baudrate;
		}
		if(StringUtils.isNotBlank(buzzType)) {
			dataArr[6] = Byte.parseByte(buzzType);
		}
		if(StringUtils.isNotBlank(ioInput)) {
			dataArr[7] = Byte.parseByte(ioInput);
		}
		if(critical != null) {
			dataArr[8] = critical;
		}
		if(filterTagTime != null) {
			dataArr[9] = filterTagTime;
		}
		if(sendInterval != null) {
			dataArr[10] = sendInterval;
		}
		if(tagType != null) {
			dataArr[11] = tagType;
		}
		if(StringUtils.isNotBlank(crcEn)) {
			dataArr[12] = Byte.parseByte(crcEn);
		}
		dataArr[13] = 0x00; // Reserver保留位 1字节
		
		// 拼接发送给硬件的指令command
		byte[] command = {0x02,0x03,0x04,0x05,0x00,0x15,0x00,0x58,0x43,0x00,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
				(byte) 0xB4};
		System.arraycopy(dataArr, 0, command, 10, 14);
		logger.info("向硬件发送的修改配置指令是{}",command);
		
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(oldDeviceId);
		if(channel == null) {
			logger.info("code-channel表中不存在设备编号【{}】的管道",oldDeviceId);
		} else {
			logger.info("设备编号【{}】对应的管道是{}",oldDeviceId,channel);
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
						if(StringUtils.isNotBlank(deviceId)) {
							CodeMapping.getInstance().updateChannelMapping(oldDeviceId, deviceId);
							if(CodeMapping.getInstance().getChannel(deviceId) == channel) {
								logger.info("code-channel表中新映射key={},value={}",deviceId,channel);
							} else {
								logger.info("code-channel表更新失败!");
							}
						}
					} else {
						logger.error("发送修改设备配置命令失败,下发命令={}",ByteUtil.byteArrToHexString(command, true));
					}
				}
			});
		}
	}
}
