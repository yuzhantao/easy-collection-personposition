package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备不存在的异常
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 608, reason = "ip地址不存在!")
public class DeviceIpNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
