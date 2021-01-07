package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;

/**
 * 统计设备在线离线数量控制器
 * 
 * @author Pingfan
 *
 */

@RestController
@RequestMapping("/device-count")
public class DeviceCountController {
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	@RequestMapping
	public ResponseEntity<?> countDeviceState(String deviceState) throws Exception {
		int online = this.personPositionDeviceService.countByDeviceState("online");
		int offline = this.personPositionDeviceService.countByDeviceState("offline");
		Map<String,Integer> ret = new HashMap<>();
		ret.put("onlineDevice", online);
		ret.put("offlineDevice", offline);
		return ResponseEntity.ok(ret);
	}
}
