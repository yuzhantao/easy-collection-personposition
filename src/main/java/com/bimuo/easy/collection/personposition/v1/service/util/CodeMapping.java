package com.bimuo.easy.collection.personposition.v1.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 编号映射类,用于根据设备编号查询对应管道(Service用)
 * 
 * @author Pingfan
 *
 */
public class CodeMapping {
	protected final static Logger logger = LogManager.getLogger(CodeMapping.class);
	public static ChannelGroup channels = new DefaultChannelGroup("ChannelGroup" + UUID.randomUUID(),
			GlobalEventExecutor.INSTANCE);
	private static ConcurrentHashMap<String, ChannelId> codeMappingList = new ConcurrentHashMap<>();

	private static CodeMapping instance;

	/**
	 * 私有化构造函数
	 */
	public CodeMapping() {
	}

	/**
	 * 单例模式编号映射类
	 * 
	 * @return
	 */
	public static synchronized CodeMapping getInstance() {
		if (instance == null) {
			instance = new CodeMapping();
		}
		return instance;
	}

	/**
	 * 根据映射获取通道
	 * 
	 * @param mappingKey 根据次字段(此指设备编号)查找指定管道实体类
	 * @return
	 */
	public Channel getChannel(String mappingKey) {
		if (codeMappingList.get(mappingKey) == null) {
			return null;
		}
		Channel channel = channels.find(codeMappingList.get(mappingKey));
		if(channel == null) {
			logger.error("编号【{}】的管道已被系统删除!",mappingKey);
			return channel;
		} else {
			return channel;
		}
	}

	/**
	 * 添加管道
	 * 
	 * @param channel
	 */
	public void addChannel(Channel channel) {
		channels.add(channel);
	}

	/**
	 * 移除管道
	 * 
	 * @param channel
	 */
	public void removeChannel(Channel channel) {
		channels.remove(channel);
	}

	/**
	 * 添加管道映射
	 * 
	 * @param key     管道对应的关键字,此类中指设备编号
	 * @param channel 管道实体,可以取得管道id
	 */
	public void addChannelMapping(String key, Channel channel) {
		codeMappingList.put(key, channel.id());
	}

	/**
	 * 是否存在该映射
	 * @param key
	 * @return
	 */
	public boolean channelMappingContainsKey(String key) {
		return codeMappingList.containsKey(key);
	}
	
	/**
	 * 获取全部设备编号(轮询设备状态使用)
	 * 
	 * @return map中存储的设备编号集合
	 */
	public List<String> findAllCode()	{
		List<String> codeList = new ArrayList<String>();
		for(int i=0; i<codeMappingList.size(); i++) {
			for(String key : codeMappingList.keySet()) {
				codeList.add(key);
			}
		}
		return codeList;
	}

	/**
	 * 删除管道映射
	 * 
	 * @param key 管道对应的关键字,此类中指设备编号
	 * @return
	 */
	public ChannelId removeChannelMapping(String key) {
		ChannelId channelId = codeMappingList.remove(key);
		return channelId;
	}

	/**
	 * 修改管道映射
	 * 
	 * @param oldKey 管道对应的旧关键字,此类中指旧设备编号
	 * @param newKey 管道对应的新关键字,此类中指新设备编号
	 * @return
	 */
	public Channel updateChannelMapping(String oldKey, String newKey) {
		Channel channel = getChannel(oldKey);
		if (oldKey.equals(newKey)) {
			return channel;
		} else {
			codeMappingList.put(newKey, channel.id());
			codeMappingList.remove(oldKey);
			return channel;
		}
	}

	/**
	 * 清空管道映射
	 */
	public void clearChannelMapping() {
		codeMappingList.clear();
	}

}
