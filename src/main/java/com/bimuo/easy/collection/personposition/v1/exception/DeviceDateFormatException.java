package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 历史查询日期格式
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 607, reason = "查询条件不是日期格式!")
public class DeviceDateFormatException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
