package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 数据库修改设备失败
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 609, reason = "数据库修改设备失败!")
public class DeviceConfigUpdateFailedException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
