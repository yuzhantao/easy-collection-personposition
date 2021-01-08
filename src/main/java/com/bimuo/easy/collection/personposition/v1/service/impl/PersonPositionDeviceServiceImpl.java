package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceAddCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeAlreadyExistsException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceDateFormatException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
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
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	
	@Override
	public PersonPositionDevice getOneByDeviceCode(String deviceCode) {
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(deviceCode);
		AssertUtils.checkArgument(ppd != null, new DeviceCodeNoneException());
		return ppd;
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
		return personPositionDeviceRepository.countByDeviceState(deviceState);
	}

//	@Override
//	public List<BrandInfo> toExcel(PersonPositionDevice dev) {
//		List<PersonPositionDevice> devices = personPositionDeviceRepository.toExcel(dev.getCreateTime(), dev.getUpdateTime());
//		List<BrandInfo> excelInfo = null; 
//		for(int i = 0; i < devices.size(); i++){ // 通过循环来赋值给另一个List
//			BrandInfo excel = new BrandInfo();
//			excel.setDeviceCode(devices.get(i).getDeviceCode());
//			excel.setDeviceState(devices.get(i).getDeviceState());
//			excel.setDeviceType(devices.get(i).getDeviceType());
//			excel.setIp(devices.get(i).getIp());
//			excel.setCreateTime(devices.get(i).getCreateTime());
//			excel.setUpdateTime(devices.get(i).getUpdateTime());
//			excelInfo.add(excel);
//		}
//		return excelInfo;
//	}
	
}
