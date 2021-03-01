package com.bimuo.easy.collection.personposition.v1.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigAllParamNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceConfigCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceConfigService;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceSettingService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.CommandStateMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceBaseConfigVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceSettingVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.NetworkParamsVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port0Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port1Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port2Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port3Vo;

/**
 * 读取修改设备配置控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices")
public class DeviceConfigController {
	private final static Logger log = LogManager.getLogger(DeviceConfigController.class);
	
//	private static int UPDATE_OVERTIME = 10000; // 修改配置等待超时时间
	
	@Autowired
	private IDeviceConfigService deviceConfigService;
	
	@Autowired
	private IDeviceSettingService deviceSettingService;
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;

	/**
	 * 读取设备配置
	 * @param deviceId 设备编号
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}/config")
	public ResponseEntity<?> queryDeviceConfig(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceSetting().getBaseConfig());
	}
	
	/**
	 * 读取网络参数
	 * @param deviceId 设备编号
	 * @return 网络参数实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}/networkParams")
	public ResponseEntity<?> queryDeviceNetworkParams(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceSetting().getNetworkParams());
	}
	
	/**
	 * 读取端口0
	 * @param deviceId 设备编号
	 * @return 端口0实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}/port0")
	public ResponseEntity<?> queryDevicePort0(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceSetting().getPort0());
	}
	
	/**
	 * 读取端口1
	 * @param deviceId 设备编号
	 * @return 端口1实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}/port1")
	public ResponseEntity<?> queryDevicePort1(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ResponseEntity.ok(ppd.getDeviceSetting().getPort1());
	}
	
//	@PutMapping(value = "/{oldDeviceId}/config")
//	public ResponseEntity<?> updateDeviceConfig(
//			@PathVariable("oldDeviceId") String oldDeviceId,
//			@RequestParam(required = false) String deviceId, 
//			@RequestParam(required = false) Byte cain1, // Spring接受参数的时候,基本数据类型需要改为包装类,或添加defaultValue,只加required=false是没用的
//			@RequestParam(required = false) Byte cain2, 
//			@RequestParam(required = false) Byte airBaudrate,
//			@RequestParam(required = false) Byte baudrate, 
//			@RequestParam(required = false) Byte buzzType,
//			@RequestParam(required = false) Byte ioInput, 
//			@RequestParam(required = false) Byte critical,
//			@RequestParam(required = false) Byte filterTagTime, 
//			@RequestParam(required = false) Byte sendInterval,
//			@RequestParam(required = false) Byte tagType, 
//			@RequestParam(required = false) Byte crcEn)
//			throws Exception {
//		// 1.先判断参数是否全空,是则停止修改报异常
//		// TODO 控制参数范围,仅判空不够
//		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
//		AssertUtils.checkArgument(cain1 != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(cain2 != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(airBaudrate != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(baudrate != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(buzzType != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(ioInput != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(critical != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(filterTagTime != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(sendInterval != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(tagType != null,new DeviceConfigAllParamNoneException());
//		AssertUtils.checkArgument(crcEn != null,new DeviceConfigAllParamNoneException());
//
//		// 2.修改硬件配置
//		deviceConfigService.updateHardwareConfig(oldDeviceId, deviceId, cain1, cain2, airBaudrate, baudrate, buzzType,
//				ioInput, critical, filterTagTime, sendInterval, tagType, crcEn);

		// 3.开线程轮询设备回复的消息记录表,map中没有设备编号或者超时就停止轮询
//		String findCode = new String();
//		if(StringUtils.isNotBlank(deviceId)) {
//			findCode = oldDeviceId.equals(deviceId) ? oldDeviceId : deviceId;
//		} else {
//			findCode = oldDeviceId;
//		}
//		return ResponseEntity.ok("修改配置指令下发成功!");
//		long startTime = System.currentTimeMillis();
//		while (true) {
//			Thread.sleep(1000);
//			long endTime = System.currentTimeMillis();
//			if (DeviceConfigResponseMapping.getInstance().findDeviceCode(findCode) == false
//					&& endTime - startTime <= UPDATE_OVERTIME) {
//				log.info("正在查找该设备……");
//				log.info("查询时间为{}ms",endTime - startTime);
//				continue; // break会跳出整个while,应该用continue
//			} else {
//				DeviceConfigResponseMapping.getInstance().removeResponseMapping(findCode); // 找到后删除该编号map记录
//				AssertUtils.checkArgument(endTime - startTime <= UPDATE_OVERTIME, new DeviceConfigUpdateTimeOutException());	
				// 4.修改数据库中对应硬件配置
//				PersonPositionDevice dev = this.personPositionDeviceService.getOneByDeviceCode(findCode); // 修改需要先查询
//				if(dev == null) { // 修改编号
//					if(DeviceConfigResponseMapping.getInstance().getUpdateConfigState(deviceId).equals("updatedAndReset")) {
//						DeviceConfigReadVo config = deviceConfigService.updateConfig(oldDeviceId, deviceId, cain1, cain2,
//								airBaudrate, baudrate, buzzType, ioInput, critical, filterTagTime, sendInterval, tagType,
//								crcEn);
//						Preconditions.checkArgument(config != null, new DeviceConfigUpdateFailedException());
//						return ResponseEntity.ok(config);
//						log.info("==========数据库【增加】设备编号【{}】配置信息成功!",deviceId);
//						return ResponseEntity.ok("增加设备成功!");
//					} else {
//						log.info("==========修改设备配置时,数据库设备编号修改失败!请重试!");
//					}
//				} else { // 修改其它属性未修改编号
//					DeviceConfigReadVo config = deviceConfigService.updateConfig(oldDeviceId, deviceId, cain1, cain2,
//							airBaudrate, baudrate, buzzType, ioInput, critical, filterTagTime, sendInterval, tagType,
//							crcEn);
//					Preconditions.checkArgument(config != null, new DeviceConfigUpdateFailedException());
//					return ResponseEntity.ok(config);
//					log.info("==========数据库【修改】设备编号【{}】配置信息成功!",oldDeviceId);
//					return ResponseEntity.ok("修改配置成功!");
//				}
//			}
//		}
//	}
	

	/**
	 * 修改设备配置
	 * 
	 * @param deviceId 设备编号
	 * @param json     设备配置的json
	 * @return         修改设备配置指令下发成功字符串
	 * @throws Exception
	 */
	@PutMapping(value = "/{deviceId}/baseConfig")
	public ResponseEntity<?> updateBaseConfig(
			@PathVariable("deviceId") String deviceId,
			@RequestBody String json) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
		DeviceBaseConfigVo baseConfig = JSONObject.parseObject(json, DeviceBaseConfigVo.class);
		// 发修改网络参数命令给硬件
		deviceConfigService.updateHardwareBaseConfig(deviceId, baseConfig.getDeviceId(), baseConfig.getCain1(), baseConfig.getCain2(), 
				baseConfig.getAirBaudrate(), baseConfig.getBaudrate(), baseConfig.getBuzzType(), baseConfig.getIoInput(),
				baseConfig.getCritical(), baseConfig.getFilterTagTime(), baseConfig.getSendInterval(), baseConfig.getTagType(), baseConfig.getCrcEn());
		// 复位重连后,通过轮询配置自动更新数据库
		return ResponseEntity.ok("修改设备配置指令下发成功!");
	}

	/**
	 * 修改网络参数
	 * 
	 * @param deviceId 设备编号
	 * @param json 网络参数的json
	 * @return     修改网络参数指令下发成功字符串
	 * @throws Exception
	 */
	@PutMapping(value = "/{deviceId}/networkParams")
	public ResponseEntity<?> updateNetworkParams(
			@PathVariable("deviceId") String deviceId,
			@RequestBody String json) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
		NetworkParamsVo networkParams = JSONObject.parseObject(json, NetworkParamsVo.class);
		// 发修改网络参数命令给硬件
		deviceSettingService.updateHardwareNetworkParams(deviceId, networkParams.getSourceIp(), networkParams.getSubnetMask(), networkParams.getGatway(), networkParams.getSourceHardware());
		// 复位重连后,通过轮询配置自动更新数据库
		return ResponseEntity.ok("修改网络参数指令下发成功!");
	}
	
	/**
	 * 修改端口0配置
	 * 
	 * @param deviceId 设备编号
	 * @param json     port0的json
	 * @return         修改端口0指令下发成功字符串
	 * @throws Exception
	 */
	@PutMapping(value = "/{deviceId}/port0")
	public ResponseEntity<?> updatePort0Config(
			@PathVariable("deviceId") String deviceId,
			@RequestBody String json) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
		Port0Vo port0 = JSONObject.parseObject(json, Port0Vo.class);
		// 发修改端口1命令给硬件
		deviceSettingService.updateHardwarePortConfig(deviceId, port0.getPortType(), port0.getSocket0DIP(), port0.getDPort(), port0.getSPort(), port0.getMode(), port0.getEnable());
		// 复位重连后,通过轮询配置自动更新数据库
		return ResponseEntity.ok("修改端口0指令下发成功!");
	}
	
	/**
	 * 修改端口1配置
	 * 
	 * @param deviceId 设备编号
	 * @param json     port1的json
	 * @return         修改端口1指令下发成功字符串
	 * @throws Exception
	 */
	@PutMapping(value = "/{deviceId}/port1")
	public ResponseEntity<?> updatePort1Config(
			@PathVariable("deviceId") String deviceId,
			@RequestBody String json) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
		Port1Vo port1 = JSONObject.parseObject(json, Port1Vo.class);
		// 发修改端口1命令给硬件
		deviceSettingService.updateHardwarePortConfig(deviceId, port1.getPortType(), port1.getSocket0DIP(), port1.getDPort(), port1.getSPort(), port1.getMode(), port1.getEnable());
		// 复位重连后,通过轮询配置自动更新数据库
		return ResponseEntity.ok("修改端口1指令下发成功!");
	}
	
	/**
	 * 修改端口2配置
	 * 
	 * @param deviceId 设备编号
	 * @param json     port2的json
	 * @return         修改端口2指令下发成功字符串
	 * @throws Exception
	 */
//	@PutMapping(value = "/{deviceId}/port2")
//	public ResponseEntity<?> updatePort2Config(
//			@PathVariable("deviceId") String deviceId,
//			@RequestBody String json) throws Exception {
//		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
//		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
//		Port2Vo port2 = JSONObject.parseObject(json, Port2Vo.class);
//		// 发修改端口1命令给硬件
//		deviceSettingService.updateHardwarePortConfig(deviceId, port2.getPortType(), port2.getSocket0DIP(), port2.getDPort(), port2.getSPort(), port2.getMode(), port2.getEnable());
//		// 复位重连后,通过轮询配置自动更新数据库
//		return ResponseEntity.ok("修改端口2指令下发成功!");
//	}
	
	/**
	 * 修改端口3配置
	 * 
	 * @param deviceId 设备编号
	 * @param json     port3的json
	 * @return         修改端口3指令下发成功字符串
	 * @throws Exception
	 */
//	@PutMapping(value = "/{deviceId}/port3")
//	public ResponseEntity<?> updatePort3Config(
//			@PathVariable("deviceId") String deviceId,
//			@RequestBody String json) throws Exception {
//		AssertUtils.checkArgument(StringUtils.isNotBlank(deviceId),new DeviceConfigCodeNoneException());
//		AssertUtils.checkArgument(StringUtils.isNotBlank(json),new DeviceConfigAllParamNoneException());
//		Port3Vo port3 = JSONObject.parseObject(json, Port3Vo.class);
//		// 发修改端口1命令给硬件
//		deviceSettingService.updateHardwarePortConfig(deviceId, port3.getPortType(), port3.getSocket0DIP(), port3.getDPort(), port3.getSPort(), port3.getMode(), port3.getEnable());
//		// 复位重连后,通过轮询配置自动更新数据库
//		return ResponseEntity.ok("修改端口3指令下发成功!");
//	}
	
	/**
	 * 复位设备
	 * @param deviceId 设备编号
	 * @return 设备配置实体
	 * @throws Exception
	 */
	@GetMapping("/{deviceId}/reset")
	public ResponseEntity<?> resetDevice(@PathVariable String deviceId) throws Exception{
		PersonPositionDevice ppd = this.personPositionDeviceService.getOneByDeviceCode(deviceId);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		this.deviceSettingService.resetHardware(deviceId);
//		if(CommandStateMapping.getInstance().getUpdateState(deviceId).equals("Success")) {
			return ResponseEntity.ok("复位指令下发成功!");
//		} else {
//			return ResponseEntity.ok("复位指令下发失败!");
//		}
	}
}