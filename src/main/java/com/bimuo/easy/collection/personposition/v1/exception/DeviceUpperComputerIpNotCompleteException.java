package com.bimuo.easy.collection.personposition.v1.exception;

import com.bimuo.easy.collection.personposition.core.http.BusinessException;
import com.bimuo.easy.collection.personposition.core.http.ResponseStatus;

/**
 * 修改数据库端口上位机ip不完整
 * 
 * @author Pingfan
 *
 */
@ResponseStatus(value = 626, reason = "修改数据库端口上位机ip不完整!")
public class DeviceUpperComputerIpNotCompleteException extends BusinessException {
	private static final long serialVersionUID = 1L;
}
