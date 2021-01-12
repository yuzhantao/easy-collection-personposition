package com.bimuo.easy.collection.personposition.v1.service;

import java.util.List;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessage;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceConfigReadVo;

/**
 * 解析电脑发出读取设备配置命令后设备回复的指令
 * 
 * @author Pingfan
 *
 */
public interface IDeviceConfigService {
	
	/**
	 * 读取设备配置信息
	 * @param deviceId 设备编号
	 * @param command 发送给硬件的指令
	 * @return 设备配置信息
	 */
	public DeviceConfigReadVo readConfig(String deviceId,String command);
	
	/**
	 * 将设备回复的指令写入内存
	 * @param msg 硬件回复的消息实体
	 * @param dconfig 设备配置信息实体
	 */
	public void addMemory(PersonPositionMessage msg, DeviceConfigReadVo dconfig);
	
	/**
	 * 根据设备编号查询回复的设备配置
	 * @param deviceId 设备编号
	 * @return 设备配置信息
	 */
	public DeviceConfigReadVo findByDeviceId(String deviceId);
	
}
