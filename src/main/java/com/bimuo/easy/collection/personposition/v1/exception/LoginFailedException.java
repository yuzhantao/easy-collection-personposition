package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

@ResponseStatus(value = 617, reason = "登陆失败!")
public class LoginFailedException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
