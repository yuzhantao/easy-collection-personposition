package com.bimuo.easy.collection.personposition.v1.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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
	
	/**
	 * 统计设备状态(在线,离线)
	 * @param deviceState
	 * @return
	 */
	int countByDeviceState(String deviceState);
	
	/**
	 * 根据开始结束时间导出数据到Excel
	 * 
	 * @return
	 */
//	@Query("SELECT * FROM PERSON_POSITION_DEVICE "
//			+ "WHERE CREATE_TIME BETWEEN :startTime AND :endTime "
//			+ "ORDER BY DEVICE_CODE DESC")
//	List<PersonPositionDevice> toExcel(Date startTime, Date endTime);
}
