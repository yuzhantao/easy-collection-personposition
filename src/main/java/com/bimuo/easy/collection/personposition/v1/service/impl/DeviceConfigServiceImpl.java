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

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

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
	public DeviceConfigReadVo updateConfig(String deviceId, byte cain1, byte cain2, byte airBaudrate, byte baudrate,
			String buzzType, String ioInput, byte critical, byte filterTagTime, byte sendInterval, byte tagType,
			String crcEn) {
		// 更新设备配置
		DeviceConfigReadVo config = new DeviceConfigReadVo();
		if (StringUtils.isNotBlank(deviceId)) {
			config.setDeviceId(deviceId.toUpperCase());
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(cain1)))) {
			config.setCain1(cain1);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(cain2)))) {
			config.setCain2(cain2);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(airBaudrate)))) {
			config.setAirBaudrate(airBaudrate);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(baudrate)))) {
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
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(critical)))) {
			config.setCritical(critical);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(filterTagTime)))) {
			config.setFilterTagTime(filterTagTime);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(sendInterval)))) {
			config.setSendInterval(sendInterval);
		}
		if (StringUtils.isNotBlank(Integer.toString(ByteUtil.byteToInt(tagType)))) {
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
		
		// TODO 怎么直接存配置信息???
		
		
		// 修改需要查询,数据库有直接更新,没有则添加一条新的设备信息
		PersonPositionDevice dev = personPositionDeviceService.getOneByDeviceCode(deviceId);
		if (dev != null) {
			dev.setDeviceState("online");
			dev.setUpdateTime(new Date());
			dev.setDeviceConfig(config);
			this.personPositionDeviceService.modify(dev);
			logger.info("==========数据库设备信息更新成功!");
		} else {
			PersonPositionDevice newDevice = new PersonPositionDevice();
			newDevice.setDeviceCode(deviceId.toUpperCase());
			newDevice.setDeviceState("online");
			newDevice.setCreateTime(new Date());
			newDevice.setUpdateTime(new Date());
			newDevice.setDeviceType("人员定位设备");
			newDevice.setDeviceConfig(config);
			personPositionDeviceService.insert(newDevice);
			logger.info("==========数据库新添加设备信息成功!");
		} 
		return config;
	}
}
