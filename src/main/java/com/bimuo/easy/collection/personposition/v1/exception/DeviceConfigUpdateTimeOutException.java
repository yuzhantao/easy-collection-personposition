package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 修改数据库设备配置超时
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 617, reason = "修改数据库设备配置超时!")
public class DeviceConfigUpdateTimeOutException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
