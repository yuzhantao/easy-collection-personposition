package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

@ResponseStatus(value = 615, reason = "登录名不能为空!")
public class LoginPasswordEmptyException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
