package com.bimuo.easy.collection.personposition.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

public interface IPersonPositionDeviceRepository extends JpaRepository<PersonPositionDevice, String>, JpaSpecificationExecutor<PersonPositionDevice> {
	
	/**
	 * 根据设备编号查询一个设备
	 * @param deviceCode
	 * @return
	 */
	PersonPositionDevice getOneByDeviceCode(String deviceCode);
	
	/**
	 * 根据设备编号删除一个设备
	 * @param deviceCode
	 * @return
	 */
	int deleteOneByDeviceCode(String deviceCode);
}
