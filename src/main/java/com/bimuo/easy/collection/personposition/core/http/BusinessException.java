package com.bimuo.easy.collection.personposition.core.http;

@ResponseStatus(value = 600, reason = "业务异常!")
public class BusinessException  extends RuntimeException {
	private static final long serialVersionUID = 1L;
}
