package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.google.common.base.Preconditions;

@RestController
@RequestMapping("/devices/person-position-devices")
public class PersonPositionDeviceController {
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	@GetMapping
	public Page<PersonPositionDevice> getAssetList(
			@RequestParam(required=false) String deviceState,
			@RequestParam(required=false) String deviceCode,
			@RequestParam(required=false) String deviceType,
			@RequestParam(required=false) String ip,
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable){
		return personPositionDeviceService.queryList(deviceState,deviceCode,deviceType,ip,pageable);
	}
	
	@GetMapping("/{deviceCode}")
	public ResponseEntity<?> queryAsset(@PathVariable String deviceCode) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceCode);
		Preconditions.checkArgument(ppd != null, "查询的设备编号" + deviceCode + "不存在!");
		return ResponseEntity.ok(ppd);
	}
	
	@PostMapping
	public ResponseEntity<?> addAsset(@RequestBody PersonPositionDevice dev) throws Exception {
		boolean isSuccess = personPositionDeviceService.insert(dev);
		dev.setCreateTime(new Date());
		Preconditions.checkArgument(isSuccess,"添加设备信息失败!");
		return ResponseEntity.ok(dev);
	}
	
	// 删除需要先查询
	@DeleteMapping("/{deviceCode}")
	public ResponseEntity<?> deleteAsset(@PathVariable String deviceCode) throws Exception {
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceCode);
		Preconditions.checkNotNull(ppd,"删除的设备不能为空!");
		boolean isSuccess = personPositionDeviceService.delete(deviceCode);
		Preconditions.checkArgument(isSuccess,"删除设备信息失败!");
		return ResponseEntity.ok(deviceCode);
	}
	
	// 修改需要先查询
	@PutMapping(value = "/{deviceCode}")
	public ResponseEntity<?> updateAsset(@PathVariable String deviceCode) throws Exception {
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceCode);
		Preconditions.checkNotNull(ppd,"修改的设备不能为空!");
		ppd.setDeviceCode(deviceCode);
		ppd.setUpdateTime(new Date());
		boolean isSuccess = personPositionDeviceService.modify(ppd);
		Preconditions.checkArgument(isSuccess,"修改设备信息失败!");
		return ResponseEntity.ok(ppd);
	}
	
	@RequestMapping(value = "/{deviceState}")
	public ResponseEntity<?> countDeviceState(@PathVariable String deviceCode,@PathVariable String deviceState) throws Exception {
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceCode);
		Preconditions.checkArgument(ppd != null, "查询的设备编号" + deviceCode + "不存在!");
		int online = this.personPositionDeviceService.countByDeviceState("online");
		int offline = this.personPositionDeviceService.countByDeviceState("offline");
		Map<String,Integer> ret = new HashMap<>();
		ret.put("onlineDevice", online);
		ret.put("offlineDevice", offline);
		return ResponseEntity.ok(ret);
//		if(ppd.getDeviceState().equals("online")) {
//			return ResponseEntity.ok(online);
//		} else if (ppd.getDeviceState().equals("offline")) {
//			return ResponseEntity.ok(offline);
//		} else {
//			return ResponseEntity.ok("未读取到设备状态,请检查设备");
//		}
	}
}
