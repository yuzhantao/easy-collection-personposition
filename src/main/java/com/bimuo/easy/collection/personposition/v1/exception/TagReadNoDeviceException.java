package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 41读取标签时数据库尚未从44中读取到设备
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 616, reason = "数据库尚未添加该设备!")
public class TagReadNoDeviceException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
