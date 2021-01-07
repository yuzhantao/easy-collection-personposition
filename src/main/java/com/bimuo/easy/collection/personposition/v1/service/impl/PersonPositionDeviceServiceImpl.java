package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.google.common.base.Preconditions;

@Transactional
@Service
public class PersonPositionDeviceServiceImpl implements IPersonPositionDeviceService {
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	
	@Override
	public PersonPositionDevice getOneByDeviceCode(String deviceCode) {
		return personPositionDeviceRepository.getOneByDeviceCode(deviceCode);
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
	public boolean insert(PersonPositionDevice dev) {
		Preconditions.checkNotNull(dev.getDeviceCode(), "添加的设备编号不能为空!");
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(dev.getDeviceCode());
		Preconditions.checkArgument(ppd == null, "添加的设备编号" + dev.getDeviceCode() + "已存在!");
		this.personPositionDeviceRepository.save(dev);
		return true;
	}
	
	@Override
	public boolean delete(String deviceCode) {
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
	
}
