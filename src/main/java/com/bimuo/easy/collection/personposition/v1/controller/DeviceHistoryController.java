package com.bimuo.easy.collection.personposition.v1.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;

/**
 * 设备历史信息控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices-history")
public class DeviceHistoryController {
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	/**
	 * 根据开始结束时间查询历史设备列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pageable 页码
	 * @return 历史设备列表
	 * @throws Exception
	 */
	@GetMapping
	public Page<PersonPositionDevice> queryDeviceHistoryList(
			@RequestParam(required=false) Date startTime,
			@RequestParam(required=false) Date endTime,
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		return personPositionDeviceService.queryHistory(startTime,endTime,pageable);
	}

}
