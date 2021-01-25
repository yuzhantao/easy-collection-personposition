package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备已存在的异常
 * 
 * @author yuzhantao
 *
 */
@ResponseStatus(value = 601, reason = "数据库已存在该设备编号!")
public class DeviceCodeAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
