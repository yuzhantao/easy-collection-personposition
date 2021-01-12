package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigReadException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceUpdateFailedException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;
import com.google.common.base.Preconditions;

/**
 * 读取修改设备配置控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices-config")
public class DeviceConfigController {
	
	@Autowired
	private IDeviceConfigService deviceConfigService;

	/**
	 * 读取设备配置信息
	 * @param deviceId 设备编号
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}")
	public ResponseEntity<?> queryDeviceConfig(@PathVariable String deviceId) throws Exception{
		DeviceConfigReadVo config = this.deviceConfigService.findByDeviceId(deviceId);
		AssertUtils.checkArgument(config != null, new DeviceConfigReadException());
		return ResponseEntity.ok(config);
	}
	
	/**
	 * 修改设备配置信息
	 * @param oldDeviceId 查询的设备编号
	 * @param deviceId 修改的设备编号
	 * @param deviceState 修改的设备状态
	 * @param deviceType 修改的设备类型
	 * @param ip 修改的ip地址
	 * @return 修改的设备配置实体
	 * @throws Exception
	 */
	@PutMapping(value = "/{oldDeviceId}")
	public ResponseEntity<?> updateDeviceConfig(@PathVariable("oldDeviceId") String oldDeviceId,
			@RequestParam(required=false) String deviceId,
			@RequestParam(required=false) String deviceState,
			@RequestParam(required=false) String deviceType,
			@RequestParam(required=false) String ip) throws Exception {
		DeviceConfigReadVo config = this.deviceConfigService.findByDeviceId(oldDeviceId); // 修改需要先查询
		Preconditions.checkArgument(config != null,new DeviceConfigReadException());
		// 判断前端传的设备编号是否为空
//		if(StringUtils.isNotBlank(deviceId)) { 
//			ppd.setDeviceCode(deviceId);
//		}
//		if(StringUtils.isNotBlank(deviceState)) {
//			ppd.setDeviceState(deviceState);
//		}
//		// 更新时间自动更改
//		ppd.setUpdateTime(new Date()); 
//		
//		if(StringUtils.isNotBlank(deviceType)) {
//			ppd.setDeviceType(deviceType);
//		}
//		if(StringUtils.isNotBlank(ip)) {
//			ppd.setIp(ip);
//		}
//		boolean isSuccess = personPositionDeviceService.modify(ppd);
//		Preconditions.checkArgument(isSuccess,new DeviceUpdateFailedException());
		return ResponseEntity.ok(config);
	}
}
