package com.bimuo.easy.collection.personposition.v1.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.annotation.EventHandle;
import com.bimuo.easy.collection.personposition.v1.service.vo.DeviceTagsVo;
import com.google.common.eventbus.EventBus;

@Service
public class PersonPositionEventBusService {
//	private Logger log = LogManager.getLogger(PersonPositionEventBusService.class);
//	
//	private EventBus eventBus = new EventBus("personposition");
//	
//	@EventHandle
//	@Autowired
//	private List<Object> eventHandles;
//	
//	/**
//	 * 系统启动后注册所有带有EventHandle注解的事件处理类
//	 */
//	@PostConstruct
//	public void init() {
//		if(this.eventHandles!=null) {
//			for(Object obj:this.eventHandles) {
//				this.register(obj);
//				log.info("注册事件处理类成功 类名:{}", obj.getClass().getName());
//			}
//		}
//	}
//	
//	/**
//	 * 系统关闭时注销所有事件处理
//	 */
//	@PreDestroy 
//	public void destory() {
//		if(this.eventHandles!=null) {
//			for(Object obj:this.eventHandles) {
//				this.eventBus.unregister(obj);
//				log.info("注销事件处理类成功 类名:{}", obj.getClass().getName());
//			}
//		}
//	}
//	
//	public void register(Object object) {
//		eventBus.register(object);
//	}
//	
	public void post(Object event) {
//		eventBus.post(event);
	}
	
//	public void postTag(DeviceTagsVo rt) {
//		this.post(rt);
//	}
}
