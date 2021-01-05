package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.google.common.base.Preconditions;

@RestController
@RequestMapping("/devices/person-position-devices")
public class PersonPositionDeviceController {
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	@PostMapping()
	public ResponseEntity<?> addAsset(@RequestBody PersonPositionDevice dev) throws Exception {
		boolean isSuccess = personPositionDeviceService.insert(dev);
		dev.setCreateTime(new Date());
		Preconditions.checkArgument(isSuccess,"添加设备信息失败!");
		return ResponseEntity.ok(dev);
	}
}
