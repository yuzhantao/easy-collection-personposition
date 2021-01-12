package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 内存中根据设备编号查询设备配置失败
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 609, reason = "内存中根据设备编号查询设备配置失败!")
public class DeviceConfigReadException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
