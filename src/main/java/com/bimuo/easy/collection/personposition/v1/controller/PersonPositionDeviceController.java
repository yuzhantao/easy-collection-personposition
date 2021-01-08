package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.exception.DeviceAddFailedException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceDeleteFailedException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceUpdateFailedException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.google.common.base.Preconditions;

/**
 * 人员定位设备CRUD控制器
 * 
 * @author yuzhantao 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices/person-position-devices")
public class PersonPositionDeviceController {
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	/**
	 * 分页查询列表接口
	 * @param deviceState 设备状态
	 * @param deviceCode 设备编号
	 * @param deviceType 设备类型
	 * @param ip ip地址
	 * @param pageable 页码
	 * @return 查询的设备列表
	 * @throws Exception
	 */
	@GetMapping
	public Page<PersonPositionDevice> queryDeviceList(
			@RequestParam(required=false) String deviceState,
			@RequestParam(required=false) String deviceCode,
			@RequestParam(required=false) String deviceType,
			@RequestParam(required=false) String ip,
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		return personPositionDeviceService.queryList(deviceState,deviceCode,deviceType,ip,pageable);
	}
	
	/**
	 * 查询单个设备接口
	 * @param deviceCode 设备编号
	 * @return 查询的设备实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceCode}")
	public ResponseEntity<?> queryDevice(@PathVariable String deviceCode) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceCode);
		return ResponseEntity.ok(ppd);
	}
	
	/**
	 * 添加设备接口
	 * @param dev 设备实体
	 * @return 添加的设备实体
	 * @throws Exception
	 */
	@PostMapping
	public ResponseEntity<?> addDevice(@RequestBody PersonPositionDevice dev) throws Exception {
		dev.setCreateTime(new Date());
		dev.setUpdateTime(new Date());
		boolean isSuccess = personPositionDeviceService.insert(dev);
		Preconditions.checkArgument(isSuccess,new DeviceAddFailedException());
		return ResponseEntity.ok(dev);
	}
	
	/**
	 * 删除设备接口
	 * @param deviceCode 设备编号
	 * @return 删除的设备编号
	 * @throws Exception
	 */
	@DeleteMapping("/{deviceCode}")
	public ResponseEntity<?> deleteDevice(@PathVariable String deviceCode) throws Exception {
		boolean isSuccess = personPositionDeviceService.delete(deviceCode);
		Preconditions.checkArgument(isSuccess,new DeviceDeleteFailedException());
		return ResponseEntity.ok(deviceCode);
	}
	
	/**
	 * 修改设备信息
	 * @param oldDeviceCode 查询的设备编号
	 * @param deviceCode 修改的设备编号
	 * @param deviceState 修改的设备状态
	 * @param deviceType 修改的设备类型
	 * @param ip 修改的ip地址
	 * @return 修改的设备实体
	 * @throws Exception
	 */
	@PutMapping(value = "/{oldDeviceCode}")
	public ResponseEntity<?> updateDevice(@PathVariable("oldDeviceCode") String oldDeviceCode,
			@RequestParam(required=false) String deviceCode,
			@RequestParam(required=false) String deviceState,
			@RequestParam(required=false) String deviceType,
			@RequestParam(required=false) String ip) throws Exception {
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(oldDeviceCode); // 修改需要先查询
		Preconditions.checkArgument(ppd != null,new DeviceCodeNoneException());
		// 判断前端传的设备编号是否为空
		if(StringUtils.isNotBlank(deviceCode)) { 
			ppd.setDeviceCode(deviceCode);
		}
		if(StringUtils.isNotBlank(deviceState)) {
			ppd.setDeviceState(deviceState);
		}
		
		// 更新时间自动更改
		ppd.setUpdateTime(new Date()); 
		
		if(StringUtils.isNotBlank(deviceType)) {
			ppd.setDeviceType(deviceType);
		}
		if(StringUtils.isNotBlank(ip)) {
			ppd.setIp(ip);
		}
		boolean isSuccess = personPositionDeviceService.modify(ppd);
		Preconditions.checkArgument(isSuccess,new DeviceUpdateFailedException());
		return ResponseEntity.ok(ppd);
	}

}
