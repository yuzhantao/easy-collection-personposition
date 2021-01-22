package com.bimuo.easy.collection.personposition.v1.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 记录修改硬件配置后设备回复的信息
 * 
 * @author Pingfan
 *
 */
public class DeviceConfigResponseMapping {

	// 存放修改硬件配置的设备编号和修改状态
	private Map<String, String> deviceConfigResponseMappingList;
	
	private static DeviceConfigResponseMapping instance;
	
	/**
	 * 私有化构造函数
	 */
	public DeviceConfigResponseMapping() {
		this.deviceConfigResponseMappingList = new HashMap<String, String>();
	}

	/**
	 * 单例模式
	 * @return
	 */
	public static synchronized DeviceConfigResponseMapping getInstance() {
		if(instance==null) {
			instance = new DeviceConfigResponseMapping();
		}
		return instance;
	}

	/**
	 * 根据映射查找设备编号
	 * @param mappingKey 此类中指设备编号
	 * @return 是否存在该设备编号
	 */
	public boolean findDeviceCode(String mappingKey) {
		return this.deviceConfigResponseMappingList.containsKey(mappingKey);
	}
	
	/**
	 * 记录修改硬件配置后设备回复的信息
	 * @param deviceId 设备编号
	 * @param isSuccess 修改状态
	 */
	public void addResponseMapping(String deviceId,String isSuccess) {
		this.deviceConfigResponseMappingList.put(deviceId, isSuccess);
	}
	
	/**
	 * 删除记录
	 * @param key 管道对应的关键字,此类中指设备编号
	 */
	public void removeResponseMapping(String key) {
		this.deviceConfigResponseMappingList.remove(key);
	}
	
	/**
	 * 清空记录表
     */
    public void clearResponseMapping() {
    	this.deviceConfigResponseMappingList.clear();
    }

}
