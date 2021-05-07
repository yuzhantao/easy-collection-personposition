//package com.bimuo.easy.collection.personposition.v1.service.util;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelId;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.util.concurrent.GlobalEventExecutor;
//
///**
// * ip映射类,用于根据ip查询指令发送时间(前台接收MQ用)
// * 
// * @author Pingfan
// *
// */
//public class IpTimeMapping {
//
//	protected final static Logger logger = LogManager.getLogger(IpTimeMapping.class);
//	private static ConcurrentHashMap<String, Date> ipTimeMappingList = new ConcurrentHashMap<>();
//
//	private static IpTimeMapping instance;
//
//	/**
//	 * 私有化构造函数
//	 */
//	public IpTimeMapping() {
//	}
//
//	/**
//	 * 单例模式ip类
//	 * 
//	 * @return
//	 */
//	public static synchronized IpTimeMapping getInstance() {
//		if (instance == null) {
//			instance = new IpTimeMapping();
//		}
//		return instance;
//	}
//
//	/**
//	 * 根据映射获取发送时间
//	 * 
//	 * @param mappingKey 根据次字段(此指ip)查找发送时间
//	 * @return
//	 */
//	public Date getDate(String mappingKey) {
//		if (ipTimeMappingList.get(mappingKey) == null) {
//			return null;
//		}
//		Date date = ipTimeMappingList.get(mappingKey);
//		if(date == null) {
//			logger.debug("编号【{}】的时间已被系统删除!",mappingKey);
//			return date;
//		} else {
//			return date;
//		}
//	}
//
//	/**
//	 * 添加时间
//	 * 
//	 * @param date
//	 */
//	public void addChannel(Date date) {
//		ipTimeMappingList.put(date);
//	}
//
//	/**
//	 * 移除管道
//	 * 
//	 * @param channel
//	 */
//	public void removeChannel(Channel channel) {
//		channels.remove(channel);
//	}
//
//	/**
//	 * 添加管道映射
//	 * 
//	 * @param key     管道对应的关键字,此类中指设备编号
//	 * @param channel 管道实体,可以取得管道id
//	 */
//	public void addChannelMapping(String key, Channel channel) {
//		codeMappingList.put(key, channel.id());
//	}
//
//	/**
//	 * 是否存在该映射
//	 * @param key
//	 * @return
//	 */
//	public boolean channelMappingContainsKey(String key) {
//		return codeMappingList.containsKey(key);
//	}
//	
//	/**
//	 * 获取全部设备编号(轮询设备状态使用)
//	 * 
//	 * @return map中存储的设备编号集合
//	 */
//	public List<String> findAllCode()	{
//		List<String> codeList = new ArrayList<String>();
//		for(int i=0; i<codeMappingList.size(); i++) {
//			for(String key : codeMappingList.keySet()) {
//				codeList.add(key);
//			}
//		}
//		return codeList;
//	}
//	
//	/**
//	 * 获取映射表全部元素(验证管道时使用)
//	 * 
//	 * @return
//	 */
//	public Map<String, ChannelId> getAll(){
//		Map<String, ChannelId> allMap = new HashMap<String, ChannelId>();
//		for (Map.Entry<String, ChannelId> entry : codeMappingList.entrySet()) {
//		    allMap.put(entry.getKey(), entry.getValue());
//		}
//		return allMap;
//	}
//
//	/**
//	 * 删除管道映射
//	 * 
//	 * @param key 管道对应的关键字,此类中指设备编号
//	 * @return
//	 */
//	public ChannelId removeChannelMapping(String key) {
//		ChannelId channelId = codeMappingList.remove(key);
//		return channelId;
//	}
//
//	/**
//	 * 修改管道映射
//	 * 
//	 * @param oldKey 管道对应的旧关键字,此类中指旧设备编号
//	 * @param newKey 管道对应的新关键字,此类中指新设备编号
//	 * @return
//	 */
//	public Channel updateChannelMapping(String oldKey, String newKey) {
//		Channel channel = getChannel(oldKey);
//		if (oldKey.equals(newKey)) {
//			return channel;
//		} else {
//			codeMappingList.put(newKey, channel.id());
//			codeMappingList.remove(oldKey);
//			return channel;
//		}
//	}
//
//	/**
//	 * 清空管道映射
//	 */
//	public void clearChannelMapping() {
//		codeMappingList.clear();
//	}
//}
