package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 修改数据库网络参数子网掩码不完整
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 623, reason = "修改数据库网络参数子网掩码不完整!")
public class DeviceNetworkSubnetNotCompleteException extends BusinessException {
	private static final long serialVersionUID = 1L;
}

