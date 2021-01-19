package com.bimuo.easy.collection.personposition.v1.service.impl;

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
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigDeviceIdException;
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
		
		PersonPositionDevice dev = this.personPositionDeviceRepository.getOneByDeviceCode(oldDeviceId);
		
		if(StringUtils.isNotBlank(deviceId)) {
			dev.setDeviceCode(deviceId.toUpperCase());
		}
		if(config != null) {
			dev.setDeviceConfig(config);
		}
		
		personPositionDeviceService.modify(dev);
		// 注意String存的是地址,用equals判断值是否相同
		if(dev.getDeviceCode().equals(dev.getDeviceConfig().getDeviceId())) {
			logger.info("==========【修改】设备编号【{}】配置信息成功!",dev.getDeviceCode());
		} else {
			logger.error("==========【修改】设备编号【{}】配置信息失败!设备编号【{}】与配置中编号【{}】不一致!",
					dev.getDeviceCode(),dev.getDeviceCode(),dev.getDeviceConfig().getDeviceId());
			new DeviceConfigDeviceIdException();
		}
		
		return config;
	}
}
