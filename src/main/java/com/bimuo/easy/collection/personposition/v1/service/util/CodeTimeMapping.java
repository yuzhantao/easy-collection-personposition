package com.bimuo.easy.collection.personposition.v1.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 编号时间映射类,用于根据设备编号查询指令发送时间(前台接收MQ用)
 * 
 * @author Pingfan
 *
 */
public class CodeTimeMapping {

	protected final static Logger logger = LogManager.getLogger(CodeTimeMapping.class);
	private static ConcurrentHashMap<String, Long> codeTimeMappingList = new ConcurrentHashMap<>();

	private static CodeTimeMapping instance;

	/**
	 * 私有化构造函数
	 */
	public CodeTimeMapping() {
	}

	/**
	 * 单例模式设备编号类
	 * 
	 * @return
	 */
	public static synchronized CodeTimeMapping getInstance() {
		if (instance == null) {
			instance = new CodeTimeMapping();
		}
		return instance;
	}

	/**
	 * 根据映射获取发送时间
	 * 
	 * @param mappingKey 根据次字段(此指设备编号)查找发送时间
	 * @return
	 */
	public Long getDate(String mappingKey) {
		if (codeTimeMappingList.get(mappingKey) == null) {
			return null;
		}
		Long date = codeTimeMappingList.get(mappingKey);
		if(date == null) {
			logger.debug("编号【{}】的时间已被系统删除!",mappingKey);
			return date;
		} else {
			return date;
		}
	}

	/**
	 * 添加时间映射
	 * 
	 * @param key     时间对应的关键字,此类中指设备编号
	 * @param date    日期实体,可以取得指令的发送时间
	 */
	public void addCodeDateMapping(String key, Long date) {
		codeTimeMappingList.put(key, date);
	}

	/**
	 * 是否存在该映射
	 * @param key  时间对应的关键字,此类中指设备编号
	 * @return
	 */
	public boolean CodeTimeMappingContainsKey(String key) {
		return codeTimeMappingList.containsKey(key);
	}
	
	/**
	 * 获取全部设备编号
	 * 
	 * @return map中存储的设备编号集合
	 */
	public List<String> findAllCode()	{
		List<String> codeList = new ArrayList<String>();
		for(int i=0; i<codeTimeMappingList.size(); i++) {
			for(String key : codeTimeMappingList.keySet()) {
				codeList.add(key);
			}
		}
		return codeList;
	}
	
	/**
	 * 获取映射表全部元素
	 * 
	 * @return
	 */
	public Map<String, Long> getAll(){
		Map<String, Long> allMap = new HashMap<String, Long>();
		for (Map.Entry<String, Long> entry : codeTimeMappingList.entrySet()) {
		    allMap.put(entry.getKey(), entry.getValue());
		}
		return allMap;
	}

	/**
	 * 删除时间映射
	 * 
	 * @param key 时间对应的关键字,此类中指设备编号
	 * @return
	 */
	public Long removeCodeTimeMapping(String key) {
		Long date = codeTimeMappingList.remove(key);
		return date;
	}

	/**
	 * 修改时间映射
	 * 
	 * @param oldKey 时间对应的旧关键字,此类中指旧ip地址
	 * @param newKey 时间对应的新关键字,此类中指新ip地址
	 * @return
	 */
	public Long updateCodeTimeMapping(String oldKey, String newKey) {
		Long date = codeTimeMappingList.get(oldKey);
		if (date == null) {
			return null;
		} else {
			if (oldKey.equals(newKey)) {
				return date;
			} else {
				codeTimeMappingList.put(newKey, date);
				codeTimeMappingList.remove(oldKey);
				return date;
			}
		}
	}

	/**
	 * 清空管道映射
	 */
	public void clearCodeTimeMapping() {
		codeTimeMappingList.clear();
	}
}
