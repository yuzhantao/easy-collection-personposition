package com.bimuo.easy.collection.personposition.v1.service;

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
	 * @return 设备配置信息
	 */
	public DeviceConfigReadVo readConfig(String deviceId);
	
	/**
	 * 修改设备配置
	 * @param deviceId 设备编号
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
	 * @return
	 */
	public DeviceConfigReadVo updateConfig(
			String oldDeviceId,
			String deviceId,
			Byte cain1,
			Byte cain2,
			Byte airBaudrate,
			Byte baudrate,
			String buzzType,
			String ioInput,
			Byte critical,
			Byte filterTagTime,
			Byte sendInterval,
			Byte tagType,
			String crcEn);
	
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
