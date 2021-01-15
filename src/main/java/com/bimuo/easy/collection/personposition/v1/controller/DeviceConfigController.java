package com.bimuo.easy.collection.personposition.v1.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigUpdateFailedException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;
import com.google.common.base.Preconditions;

/**
 * 读取修改设备配置控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@EnableAsync // 异步处理
@RequestMapping("/device-config")
public class DeviceConfigController {
	
	@Autowired
	private IDeviceConfigService deviceConfigService;
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;

	/**
	 * 读取设备配置信息
	 * @param deviceId 设备编号
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}")
	public ResponseEntity<?> queryDeviceConfig(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceConfig());
	}
	
	/**
	 * 修改设备配置信息
	 * @param oldDeviceId 原本的设备编号
	 * @param deviceId 修改的设备编号
	 * @param cain1 发送增益,范围0~3
	 * @param cain2 接收增益,范围0~31
	 * @param airBaudrate 空中波特率,范围0~2(0:250K, 1:1M, 2:2M)
	 * @param baudrate 串口波特率,范围0~6(4800~115200)
	 * @param buzzType 蜂鸣器状态,范围0~1(0:关,1:开)
	 * @param ioInput 地感值,范围0~1(0:无地感,1:有地感)
	 * @param critical 两秒内接收到的同一个ID的次数阀值,范围0~8
	 * @param filterTagTime 同一个ID的过滤时间,单位秒,范围0~250
	 * @param sendInterval 两个韦根数据的发送间隔,单位0.1秒,范围0~250
	 * @param tagType 标签类型,范围0~255
	 * @param crcEn 设备CRC状态,范围0~1(0:取消,1:有效)
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@PutMapping(value = "/{oldDeviceId}")
	public ResponseEntity<?> updateDeviceConfig(@PathVariable("oldDeviceId") String oldDeviceId,
			@RequestParam(required=false) String deviceId,
			@RequestParam(required=false) String cain1, // Spring接受参数的时候,基本数据类型需要改为包装类,或添加defaultValue,只加required=false是没用的
			@RequestParam(required=false) String cain2,
			@RequestParam(required=false) String airBaudrate,
			@RequestParam(required=false) String baudrate,
			@RequestParam(required=false) String buzzType,
			@RequestParam(required=false) String ioInput,
			@RequestParam(required=false) String critical,
			@RequestParam(required=false) String filterTagTime,
			@RequestParam(required=false) String sendInterval,
			@RequestParam(required=false) String tagType,
			@RequestParam(required=false) String crcEn) throws Exception {
		PersonPositionDevice dev = this.personPositionDeviceService.getOneByDeviceCode(oldDeviceId); // 修改需要先查询
		Preconditions.checkArgument(dev != null,new DeviceCodeNoneException());
		DeviceConfigReadVo config = deviceConfigService.updateConfig(deviceId, Byte.parseByte(cain1), Byte.parseByte(cain2), Byte.parseByte(airBaudrate), Byte.parseByte(baudrate), buzzType, ioInput, Byte.parseByte(critical), Byte.parseByte(filterTagTime), Byte.parseByte(sendInterval), Byte.parseByte(tagType), crcEn);
		// TODO 为什么null没有报异常
		Preconditions.checkArgument(config != null,new DeviceConfigUpdateFailedException());
		return ResponseEntity.ok(config);
	}
}
