package com.bimuo.easy.collection.personposition.v1.service.util;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 编号映射类,用于根据设备编号查询对应管道
 * 
 * @author Pingfan
 *
 */
public class CodeMapping {
	public ChannelGroup channels;
	private ConcurrentHashMap<String, ChannelId> codeMappingList;
	
	private static CodeMapping instance;

	/**
	 * 私有化构造函数
	 */
	public CodeMapping() {
		this.codeMappingList = new ConcurrentHashMap<String, ChannelId>();
		this.channels = new DefaultChannelGroup("ChannelGroup"+UUID.randomUUID(),GlobalEventExecutor.INSTANCE);
	}

	/**
	 * 单例模式编号映射类
	 * @return
	 */
	public static synchronized CodeMapping getInstance() {
		if(instance==null) {
			instance = new CodeMapping();
		}
		return instance;
	}
	
	/**
	 * 根据映射获取通道
	 * @param mappingKey 根据次字段查找指定管道实体类
	 * @return
	 */
	public Channel getChannel(String mappingKey) {
		return this.channels.find(this.codeMappingList.get(mappingKey));
	}
	
	/**
	 * 添加管道
	 * @param channel
	 */
	public void addChannel(Channel channel) {
		this.channels.add(channel);	
	}
	
	/**
	 * 移除管道
	 * @param channel
	 */
	public void removeChannel(Channel channel) {
		this.channels.remove(channel);
	}
	
	/**
	 * 添加管道映射
	 * @param key 管道对应的关键字,此类中指设备编号
	 * @param channel 管道实体,可以取得管道id
	 */
	public void addChannelMapping(String key,Channel channel) {
		this.codeMappingList.put(key, channel.id());
	}
	
	/**
	 * 删除管道映射
	 * @param key 管道对应的关键字,此类中指设备编号
	 * @return
	 */
	public Channel removeChannelMapping(String key) {
		Channel channel = getChannel(key);
		this.codeMappingList.remove(key);
		return channel;
	}
	
	/**
     * 清空管道映射
     */
    public void clearChannelMapping() {
    	this.codeMappingList.clear();
    }
	
}
