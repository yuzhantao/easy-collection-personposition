package com.bimuo.easy.collection.personposition.core.util;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 网络映射类，用于绑定机柜编号与IP地址
 * @author yuzhantao
 *
 */
public class NetMapping {
	public ChannelGroup channels;
	private ConcurrentHashMap<String,ChannelId> netMappingList;
	
	private static NetMapping instance;
	/**
	 * 私有化构造函数，避免实例化多�?
	 */
	private NetMapping() {
		this.netMappingList=new ConcurrentHashMap<String,ChannelId>();
		this.channels = new DefaultChannelGroup("ChannelGroup"+UUID.randomUUID(),GlobalEventExecutor.INSTANCE);
	}
	/**
	 * 单利模式网络映射管理�?
	 * @return
	 */
    public static synchronized NetMapping getInstance(){
        if (instance == null) {
            instance = new NetMapping();
        }
        return instance;
    }
    /**
     * 添加通道
     * @param channel
     */
    public void addChannel(Channel channel) {
    	this.channels.add(channel);
    }
    /**
     * 移除通道
     * @param channel
     */
    public void removeChannel(Channel channel) {
    	this.channels.remove(channel);
    }
    /**
     * 根据映射获取通道
     * @param mappingKey 根据次字段查找指定管道实体类
     * @return
     */
    public Channel getChannel(String mappingKey) {
    	return this.channels.find(this.netMappingList.get(mappingKey));
    }
    /**
     * 添加管道映射
     * @param key		 管道对应的关键字
     * @param channelId	 管道实体�?
     */
    public void addChannelMapping(String key,Channel channel) {
    	this.netMappingList.put(key, channel.id());
    }
    /**
     * 移除管道映射
     * @param key 要移除的管道关键�?
     * @return
     */
    public Channel removeChannelMapping(String key) {
    	Channel channel = getChannel(key);
    	this.netMappingList.remove(key);
    	return channel;
    }
    /**
     * 晴空管道映射
     */
    public void clearChannelMapping() {
    	this.netMappingList.clear();
    }
}