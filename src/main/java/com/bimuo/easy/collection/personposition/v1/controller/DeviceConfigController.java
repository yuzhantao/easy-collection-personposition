package com.bimuo.easy.collection.personposition.v1.controller;

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
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigAllParamNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigUpdateFailedException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.DeviceConfigResponseMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;
import com.google.common.base.Preconditions;

/**
 * 读取修改设备配置控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices")
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
	@GetMapping("/{deviceId}/config")
	public ResponseEntity<?> queryDeviceConfig(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceConfig());
	}
	
	/**
	 * 修改设备配置信息
	 * 
	 * @param oldDeviceId   原本的设备编号
	 * @param deviceId      修改的设备编号
	 * @param cain1         发送增益,范围0~3
	 * @param cain2         接收增益,范围0~31
	 * @param airBaudrate   空中波特率,范围0~2(0:250K, 1:1M, 2:2M)
	 * @param baudrate      串口波特率,范围0~6(4800~115200)
	 * @param buzzType      蜂鸣器状态,范围0~1(0:关,1:开)
	 * @param ioInput       地感值,范围0~1(0:无地感,1:有地感)
	 * @param critical      两秒内接收到的同一个ID的次数阀值,范围0~8
	 * @param filterTagTime 同一个ID的过滤时间,单位秒,范围0~250
	 * @param sendInterval  两个韦根数据的发送间隔,单位0.1秒,范围0~250
	 * @param tagType       标签类型,范围0~255
	 * @param crcEn         设备CRC状态,范围0~1(0:取消,1:有效)
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@PutMapping(value = "/{oldDeviceId}/config")
	public ResponseEntity<?> updateDeviceConfig(@PathVariable("oldDeviceId") String oldDeviceId,
			@RequestParam(required = false) String deviceId, @RequestParam(required = false) Byte cain1, // Spring接受参数的时候,基本数据类型需要改为包装类,或添加defaultValue,只加required=false是没用的
			@RequestParam(required = false) Byte cain2, @RequestParam(required = false) Byte airBaudrate,
			@RequestParam(required = false) Byte baudrate, @RequestParam(required = false) String buzzType,
			@RequestParam(required = false) String ioInput, @RequestParam(required = false) Byte critical,
			@RequestParam(required = false) Byte filterTagTime, @RequestParam(required = false) Byte sendInterval,
			@RequestParam(required = false) Byte tagType, @RequestParam(required = false) String crcEn)
			throws Exception {
		// 1.先判断参数是否全空,是则停止修改报异常
		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId) && cain1 != null && cain2 != null && airBaudrate != null && 
				baudrate != null && StringUtils.isNotBlank(buzzType) && StringUtils.isNotBlank(ioInput) && critical != null && 
				filterTagTime != null && sendInterval != null && tagType != null && StringUtils.isNotBlank(crcEn),
				new DeviceConfigAllParamNoneException());
		
		// 2.修改硬件配置
		deviceConfigService.updateHardwareConfig(oldDeviceId, deviceId, cain1, cain2, airBaudrate, baudrate, buzzType,
				ioInput, critical, filterTagTime, sendInterval, tagType, crcEn);

		// 3.开线程轮询设备回复的消息记录表,map中没有设备编号或者超时就停止轮询
		String findCode = new String();
		if(StringUtils.isNotBlank(deviceId)) {
			findCode = oldDeviceId.equals(deviceId) ? oldDeviceId : deviceId;
		} else {
			findCode = oldDeviceId;
		}
		long startTime = System.currentTimeMillis();
		while (true) {
			Thread.sleep(1000);
			long endTime = System.currentTimeMillis();
			if (DeviceConfigResponseMapping.getInstance().findDeviceCode(findCode) == false
					|| endTime - startTime > 10000) {
				continue; // break会跳出整个while,应该用continue
			} else {
				DeviceConfigResponseMapping.getInstance().removeResponseMapping(findCode); // 找到后删除该编号map记录
				// 4.修改数据库中对应硬件配置
				PersonPositionDevice dev = this.personPositionDeviceService.getOneByDeviceCode(oldDeviceId); // 修改需要先查询
				Preconditions.checkArgument(dev != null, new DeviceCodeNoneException());
				DeviceConfigReadVo config = deviceConfigService.updateConfig(oldDeviceId, deviceId, cain1, cain2,
						airBaudrate, baudrate, buzzType, ioInput, critical, filterTagTime, sendInterval, tagType,
						crcEn);
				Preconditions.checkArgument(config != null, new DeviceConfigUpdateFailedException());
				return ResponseEntity.ok(config);
			}
		}
	}
}
