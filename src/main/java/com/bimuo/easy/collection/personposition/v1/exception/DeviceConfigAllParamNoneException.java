package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 修改设备配置时参数全为空
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 611, reason = "修改设备配置时参数全为空!")
public class DeviceConfigAllParamNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
