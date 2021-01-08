package com.bimuo.easy.collection.personposition.v1.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

public interface IPersonPositionDeviceRepository extends JpaRepository<PersonPositionDevice, String>, JpaSpecificationExecutor<PersonPositionDevice> {
	
	/**
	 * 根据设备编号查询一个设备
	 * @param deviceCode 设备编号
	 * @return 查询到的设备实体
	 */
	PersonPositionDevice getOneByDeviceCode(String deviceCode);
	
	/**
	 * 根据设备编号删除一个设备
	 * @param deviceCode 设备编号
	 * @return 删除的设备个数
	 */
	int deleteOneByDeviceCode(String deviceCode);
	
	/**
	 * 统计设备状态(在线,离线)
	 * @param deviceState 设备状态
	 * @return 在线离线的设备数量
	 */
	int countByDeviceState(String deviceState);
}
