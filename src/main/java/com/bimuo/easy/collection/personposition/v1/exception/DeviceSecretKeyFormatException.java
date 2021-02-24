package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 新旧密钥位数不完整
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 628, reason = "新旧密钥位数不完整!")
public class DeviceSecretKeyFormatException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
