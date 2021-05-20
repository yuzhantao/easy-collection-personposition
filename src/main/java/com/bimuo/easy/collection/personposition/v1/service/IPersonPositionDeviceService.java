package com.bimuo.easy.collection.personposition.v1.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

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
	 * @return 实时设备列表
	 */
	Page<PersonPositionDevice> queryList(String deviceState,String deviceCode,String deviceType,String ip,Pageable pageable);
	
	/**
	 * 根据开始结束时间分页查询历史
	 * @param startTime
	 * @param endTime
	 * @param pageable
	 * @return 历史设备列表
	 */
	Page<PersonPositionDevice> queryHistory(Date startTime,Date endTime,Pageable pageable);
	
	/**
	 * 根据开始结束时间不分页查询历史(供Excel导出用)
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<PersonPositionDevice> queryHistoryList(Date startTime,Date endTime);
	
	/**
	 * 根据设备编号查询单个设备
	 * @param deviceCode 设备编号
	 * @return 查询的设备实体
	 */
	PersonPositionDevice getOneByDeviceCode(String deviceCode);
	
	/**
	 * 根据ip地址查询离线的设备集合
	 * @param ip ip地址
	 * @return 设备实体集合
	 */
	List<PersonPositionDevice> getOfflineDevicesByIp(String ip);
	
	/**
	 * 添加人员定位设备
	 * @param dev 设备实体
	 * @return 是否添加成功
	 */
	public boolean insert(PersonPositionDevice dev);
	
	/**
	 * 删除人员定位设备
	 * @param deviceCode 设备编号
	 * @return 是否删除成功
	 */
	public boolean delete(String deviceCode);

	/**
	 * 修改设备配置信息
	 * @param dev 设备实体
	 * @return 是否修改成功
	 */
	public boolean modify(PersonPositionDevice dev);
	
	/**
	 * 统计在线离线数量
	 * @param deviceState 设备状态
	 * @return 在线离线设备数量
	 */
	public int countByDeviceState(String deviceState);
	
	/**
	 * 更新有效设备的状态为离线
	 */
	void updateDeviceOffline();
}
