package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备编号不存在的异常
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 602, reason = "数据库不存在该设备编号!")
public class DeviceCodeNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
