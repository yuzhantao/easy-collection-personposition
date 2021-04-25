package com.bimuo.easy.collection.personposition.v1.task;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.impl.DeviceConfigServiceImpl;

/**
 * 系统关闭时，将所有设备状态改为离线(改为项目启动成功后执行)
 * @author yuzhantao
 *
 */
@Service
public class DeviceOfflineOfRebootTask implements ApplicationRunner {
	protected final static Logger logger = LogManager.getLogger(DeviceOfflineOfRebootTask.class);
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.debug("设备已全部离线");
		personPositionDeviceService.updateDeviceOffline();
	}
	
//	@PreDestroy
//	public void reboot() {
//		logger.info("设备已全部离线");
//		personPositionDeviceService.updateDeviceOffline();
//	}
}
