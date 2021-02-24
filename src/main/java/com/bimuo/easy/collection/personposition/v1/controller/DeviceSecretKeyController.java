package com.bimuo.easy.collection.personposition.v1.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigAllParamNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceSecretKeyNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceSecretKeyService;
import com.bimuo.easy.collection.personposition.v1.service.vo.secretKey.Key1Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.NetworkParamsVo;

/**
 * 修改设备密钥控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices")
public class DeviceSecretKeyController {
	private final static Logger log = LogManager.getLogger(DeviceConfigController.class);
	
	@Autowired
	private IDeviceSecretKeyService deviceSecretKeyService;
	
	/**
	 * 修改密钥
	 * 
	 * @param deviceId     设备编号
	 * @param oldPassword  旧密钥
	 * @param newPassword  新密钥
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/{deviceId}/secretKey1")
	public ResponseEntity<?> updatesecretKey1(
			@PathVariable("deviceId") String deviceId,
			@RequestParam(required = false) String oldPassword,
			@RequestParam(required = false) String newPassword) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNotBlank(oldPassword),new DeviceSecretKeyNotCompleteException());
		AssertUtils.checkArgument(StringUtils.isNotBlank(newPassword),new DeviceSecretKeyNotCompleteException());
		// 发修改密钥命令给硬件
		deviceSecretKeyService.updateHardwareSecretKey(deviceId, oldPassword, newPassword);
		// 不存数据库,硬件自行判断修改正确与否
		return ResponseEntity.ok("密钥修改成功!");
	}
	

}
