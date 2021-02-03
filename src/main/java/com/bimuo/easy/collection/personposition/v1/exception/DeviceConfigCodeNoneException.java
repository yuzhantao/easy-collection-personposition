package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 修改设备配置未传设备编号的异常
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 615, reason = "修改设备配置时编号为空!")
public class DeviceConfigCodeNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
