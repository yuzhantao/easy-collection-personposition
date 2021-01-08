package com.bimuo.easy.collection.personposition.v1.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.vo.BrandInfo;

/**
 * 人员定位相关服务
 * @author yuzhantao
 * @author Pingfan
 *
 */
public interface IPersonPositionDeviceService {
	
	/**
	 * 分页查询所有
	 * @param deviceState
	 * @param deviceCode
	 * @param deviceType
	 * @param ip
	 * @param pageable
	 * @return
	 */
	Page<PersonPositionDevice> queryList(String deviceState,String deviceCode,String deviceType,String ip,Pageable pageable);
	
	/**
	 * 根据设备编号查询单个设备
	 * @param deviceCode
	 * @return
	 */
	PersonPositionDevice getOneByDeviceCode(String deviceCode);
	
	/**
	 * 添加人员定位设备
	 * @param dev
	 * @return
	 */
	public boolean insert(PersonPositionDevice dev);
	
	/**
	 * 删除人员定位设备
	 * @param deviceCode
	 * @return
	 */
	public boolean delete(String deviceCode);

	/**
	 * 修改设备配置信息
	 * @param deviceCode
	 * @return
	 */
	public boolean modify(PersonPositionDevice dev);
	
	/**
	 * 统计在线离线数量
	 * @param deviceState
	 * @return
	 */
	public int countByDeviceState(String deviceState);
	
//	/**
//	 * 导出数据到Excel
//	 * 
//	 * @return
//	 */
//	List<BrandInfo> toExcel(PersonPositionDevice dev);

}
