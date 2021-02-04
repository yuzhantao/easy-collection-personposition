package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

@ResponseStatus(value = 616, reason = "用户不存在!")
public class UserNotExistException extends BusinessException {
	private static final long serialVersionUID = 1L;
}