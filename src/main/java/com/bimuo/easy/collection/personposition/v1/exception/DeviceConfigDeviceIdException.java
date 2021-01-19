package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备编号与配置中不一致
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 610, reason = "设备编号与配置中不一致!")
public class DeviceConfigDeviceIdException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
