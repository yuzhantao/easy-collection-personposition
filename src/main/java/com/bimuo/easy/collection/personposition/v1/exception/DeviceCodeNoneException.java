package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备不存在的异常
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 602, reason = "设备编号不存在!")
public class DeviceCodeNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;

}
