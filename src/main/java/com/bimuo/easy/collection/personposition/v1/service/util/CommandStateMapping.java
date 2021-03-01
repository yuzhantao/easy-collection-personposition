package com.bimuo.easy.collection.personposition.v1.service.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录命令下发的状态(Controller用)
 * 
 * @author Pingfan
 *
 */
public class CommandStateMapping {

	// 存放修改硬件配置的设备编号和修改状态
	private ConcurrentHashMap<String, String> commandStateMappingList;

	private static CommandStateMapping instance;

	/**
	* 私有化构造函数
	*/
	public CommandStateMapping() {
		this.commandStateMappingList = new ConcurrentHashMap<String, String>();
	}

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static synchronized CommandStateMapping getInstance() {
		if (instance == null) {
			instance = new CommandStateMapping();
		}
		return instance;
	}

	/**
	 * 根据映射查找设备编号
	 * 
	 * @param mappingKey 此类中指设备编号
	 * @return 是否存在该设备编号
	 */
	public boolean findDeviceCode(String mappingKey) {
		return this.commandStateMappingList.containsKey(mappingKey);
	}

	/**
	 * 根据映射获取通道
	 * 
	 * @param mappingKey 根据次字段(此指设备编号)查找指定管道实体类
	 * @return
	 */
	public String getUpdateState(String mappingKey) {
		if (this.commandStateMappingList.get(mappingKey) == null) {
			return null;
		}
		return this.commandStateMappingList.get(mappingKey);
	}

	/**
	 * 记录修改硬件配置后设备回复的信息
	 * 
	 * @param deviceId  设备编号
	 * @param isSuccess 修改状态
	 */
	public void addStateMapping(String deviceId, String isSuccess) {
		this.commandStateMappingList.put(deviceId, isSuccess);
	}

	/**
	 * 删除记录
	 * 
	 * @param key 管道对应的关键字,此类中指设备编号
	 */
	public void removeStateMapping(String key) {
		this.commandStateMappingList.remove(key);
	}

	/**
	 * 清空记录表
	 */
	public void clearStateMapping() {
		this.commandStateMappingList.clear();
	}

}
