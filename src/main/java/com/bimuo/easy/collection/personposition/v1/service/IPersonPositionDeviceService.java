package com.bimuo.easy.collection.personposition.v1.service;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

/**
 * 人员定位相关服务
 * @author yuzhantao
 *
 */
public interface IPersonPositionDeviceService {
	/**
	 * 添加人员定位设备
	 * @param dev
	 * @return
	 */
	public boolean insert(PersonPositionDevice dev);
}
