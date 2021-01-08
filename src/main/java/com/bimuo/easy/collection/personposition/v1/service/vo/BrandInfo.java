package com.bimuo.easy.collection.personposition.v1.service.vo;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 使用easypoi导出excel映射实体注解
 * 
 * @author Pingfan
 *
 */
@Data
public class BrandInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "deviceCode", width = 25,orderNum = "0")
    private String deviceCode;
 
    @Excel(name = "deviceState", width = 25,orderNum = "0")
    private String deviceState;
 
    @Excel(name = "deviceType", width = 25,orderNum = "0")
    private String deviceType;
 
    @Excel(name = "ip", width = 25,orderNum = "0")
    private String ip;
 
    @Excel(name = "createTime",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    private Date createTime;
 
    @Excel(name = "updateTime",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    private Date updateTime;

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
}
