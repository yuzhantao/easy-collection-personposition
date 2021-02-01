package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 数据库添加标签失败
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 614, reason = "数据库添加标签失败!")
public class TagHistoryAddFailedException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
