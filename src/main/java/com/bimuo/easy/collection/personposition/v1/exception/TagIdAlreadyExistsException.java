package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 设备已存在的异常
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 613, reason = "数据库已存在该标签编号!")
public class TagIdAlreadyExistsException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
