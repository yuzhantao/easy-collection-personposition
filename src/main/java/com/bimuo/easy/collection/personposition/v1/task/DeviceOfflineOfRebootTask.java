package com.bimuo.easy.collection.personposition.v1.task;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;

/**
 * 系统关闭时，将所有设备状态改为离线
 * @author yuzhantao
 *
 */
@Service
public class DeviceOfflineOfRebootTask {
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	@PreDestroy
	public void reboot() {
		personPositionDeviceService.updateDeviceOffline();
	}
}
