package com.bimuo.easy.collection.personposition.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.google.common.base.Preconditions;

@Service
public class PersonPositionDeviceServiceImpl implements IPersonPositionDeviceService {
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;

	@Override
	public boolean insert(PersonPositionDevice dev) {
		Preconditions.checkNotNull(dev.getDeviceCode(), "添加的设备编号不能为空!");
		PersonPositionDevice ppd = this.personPositionDeviceRepository.getOneByDeviceCode(dev.getDeviceCode());
		Preconditions.checkArgument(ppd == null, "添加的设备编号" + dev.getDeviceCode() + "已存在!");
		this.personPositionDeviceRepository.save(dev);
		return true;
	}

}
