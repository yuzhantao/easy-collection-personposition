package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 前端传的新旧密钥不完整
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 627, reason = "前端传的新旧密钥不完整!")
public class DeviceSecretKeyNotCompleteException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
