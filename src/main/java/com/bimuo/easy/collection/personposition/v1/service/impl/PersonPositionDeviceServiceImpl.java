package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceAddCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeAlreadyExistsException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceIpNoneException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.mqtt.IMqttMessageSenderService;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.BrandInfo;
import com.google.common.base.Preconditions;

/**
 * 人员定位相关服务实现类
 * 
 * @author yuzhantao
 * @author Pingfan
 *
 */
@Transactional
@Service
public class PersonPositionDeviceServiceImpl implements IPersonPositionDeviceService {
	private final static Logger log = LogManager.getLogger(PersonPositionDeviceServiceImpl.class);
	
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	
	@Autowired
	private IMqttMessageSenderService mqttMessageSenderService;
	
	@Override
	public PersonPositionDevice getOneByDeviceCode(String deviceCode) {
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(deviceCode);
		return ppd;
	}
	
	@Override
	public List<PersonPositionDevice> getOfflineDevicesByIp(String ip) {
		// 将主动断开的设备改为无效
		List<PersonPositionDevice> activeOfflineDivices = this.personPositionDeviceRepository.getOneByIp(ip);
		if(activeOfflineDivices.isEmpty()) {
			// TODO 断开出现在修改成功之后ip就不存在了
			log.info("旧ip={}已更新",ip);
			//log.error("管道异常,数据库不存在ip={},更新数据库失败!",ip);
			//AssertUtils.checkArgument(activeOfflineDivices.isEmpty() == false, new DeviceIpNoneException());
		} else {
			for(int i=0; i<activeOfflineDivices.size(); i++) {
				if(activeOfflineDivices.get(i).isEffective() == false) { // 修改配置时已设置为无效记录
					activeOfflineDivices.get(i).setDeviceState("offline");
					activeOfflineDivices.get(i).setUpdateTime(new Date());
					personPositionDeviceRepository.save(activeOfflineDivices.get(i));
					log.error("ip={}修改配置后断开,状态为{}",ip, activeOfflineDivices.get(i).getDeviceState());
					// 离线时删除code-channel映射,以备修改配置使用
					CodeMapping.getInstance().removeChannelMapping(activeOfflineDivices.get(i).getDeviceCode());  // 避免断开连接时netty自动将管道清除
					if(CodeMapping.getInstance().channelMappingContainsKey(activeOfflineDivices.get(i).getDeviceCode()) == false) {
						log.debug("复位后设备【{}】映射删除成功",activeOfflineDivices.get(i).getDeviceCode());
					} else {
						log.error("复位后设备【{}】映射删除失败或并无该记录",activeOfflineDivices.get(i).getDeviceCode());
					}
				} else { // 主动断开是有效记录,仅把状态改为离线
					activeOfflineDivices.get(i).setDeviceState("offline");
					log.error("ip={}主动断开,状态为{}",ip, activeOfflineDivices.get(i).getDeviceState());
					this.personPositionDeviceRepository.save(activeOfflineDivices.get(i));
					// 发送到mqtt,刷新页面设备状态为离线
					String topic = "personpositon/" + activeOfflineDivices.get(i).getDeviceCode() + "/config";
					String mqttData = JSONArray.toJSON(activeOfflineDivices.get(i).getDeviceSetting().getBaseConfig()).toString();
					if (StringUtils.isNotBlank(topic)) {
						mqttMessageSenderService.sendToMqtt(topic, mqttData);
						log.debug("【{}】发送mqtt消息完成,主题:{},消息:{}", activeOfflineDivices.get(i).getDeviceCode(), topic, mqttData);
					} else {
						log.error("【{}】发送mqtt消息失败,主题:{},消息:{}", activeOfflineDivices.get(i).getDeviceCode(), topic, mqttData);
					}
				}
			}	
		}
		// 将断开的设备(改配置和主动断开的查出后状态改为offline)
//		List<PersonPositionDevice> devices = this.personPositionDeviceRepository.getOneByIpAndIsEffective(ip,false); // 收到4F4B后已设置失效,所以断线重连时将失效的设备记录改为offline
//		AssertUtils.checkArgument(devices.isEmpty() == false, new DeviceIpNoneException());
		return activeOfflineDivices;
	}
	
	@Override
	public Page<PersonPositionDevice> queryList(String deviceState,String deviceCode,String deviceType,String ip,Pageable pageable) {
		Specification<PersonPositionDevice> specification = new Specification<PersonPositionDevice>() {
			
			private static final long serialVersionUID = 1L;
			
			public Predicate toPredicate(Root<PersonPositionDevice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(deviceState)) {
                    predicates.add(cb.equal(root.<String>get("deviceState"), deviceState));
                }
                if (StringUtils.isNotBlank(deviceCode)) {
                    predicates.add(cb.like(root.get("deviceCode").as(String.class), "%"+deviceCode+"%"));
                }
                if (StringUtils.isNotBlank(deviceType)) {
                    predicates.add(cb.equal(root.<String>get("deviceType"), deviceType));
                }
                if (StringUtils.isNotBlank(ip)) {
                    predicates.add(cb.like(root.get("ip").as(String.class), "%"+ip+"%"));
                }
                predicates.add(cb.equal(root.get("isEffective").as(boolean.class), 1)); // 只显示有效数据isEffective==1
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
		Page<PersonPositionDevice> pages = personPositionDeviceRepository.findAll(specification,pageable);
		return pages;
	}
	
	@Override
	public Page<PersonPositionDevice> queryHistory(Date startTime, Date endTime, Pageable pageable) {
		Specification<PersonPositionDevice> specification = new Specification<PersonPositionDevice>() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public Predicate toPredicate(Root<PersonPositionDevice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 判定开始结束时间是否为空
                if (startTime != null) {
                    //大于或等于开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), startTime));
                }
                if (endTime != null) {
                    //小于或等于结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), endTime));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<PersonPositionDevice> pages = personPositionDeviceRepository.findAll(specification,pageable);
		return pages;
	}
	
	
	@Override
	public List<PersonPositionDevice> queryHistoryList(Date startTime, Date endTime) {
		Specification<PersonPositionDevice> specification = new Specification<PersonPositionDevice>() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public Predicate toPredicate(Root<PersonPositionDevice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 判定开始结束时间是否为空
                if (startTime != null) {
                    //大于或等于开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), startTime));
                }
                if (endTime != null) {
                    //小于或等于结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), endTime));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<PersonPositionDevice> devices = personPositionDeviceRepository.findAll(specification);
		return devices;
	}
	
	@Override
	public boolean insert(PersonPositionDevice dev) {
		Preconditions.checkNotNull(dev.getDeviceCode(), new DeviceAddCodeNoneException());
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(dev.getDeviceCode());
		AssertUtils.checkArgument(ppd == null, new DeviceCodeAlreadyExistsException());
		this.personPositionDeviceRepository.save(dev);
		return true;
	}
	
	@Override
	public boolean delete(String deviceCode) {
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(deviceCode); // 删除需要先查询
		Preconditions.checkArgument(ppd != null,new DeviceCodeNoneException());
		return this.personPositionDeviceRepository.deleteOneByDeviceCode(deviceCode) > 0;
	}

	@Override
	public boolean modify(PersonPositionDevice dev) {
		this.personPositionDeviceRepository.save(dev);
		return true;
	}

	@Override
	public int countByDeviceState(String deviceState) {
		return personPositionDeviceRepository.countByDeviceStateAndIsEffective(deviceState,true);
	}

	@Override
	public List<BrandInfo> toExcel(Date startTime,Date endTime) {
		List<PersonPositionDevice> devices = queryHistoryList(startTime, endTime);
		List<BrandInfo> excelInfo = new ArrayList<>(); 
		for(int i = 0; i < devices.size(); i++){ // 通过循环来赋值给另一个List
			BrandInfo excel = new BrandInfo();
			excel.setDeviceCode(devices.get(i).getDeviceCode());
			excel.setDeviceState(devices.get(i).getDeviceState());
			excel.setDeviceType(devices.get(i).getDeviceType());
			excel.setIp(devices.get(i).getIp());
			excel.setCreateTime(devices.get(i).getCreateTime());
			excel.setUpdateTime(devices.get(i).getUpdateTime());
			excelInfo.add(excel);
		}
		return excelInfo;
	}

	@Override
	public void updateDeviceOffline() {
		personPositionDeviceRepository.updateAllDeviceState("offline");
	}
}
