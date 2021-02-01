package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 添加的标签编号为空
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 612, reason = "添加的标签编号为空!")
public class TagAddIdNoneException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
