package com.bimuo.easy.collection.personposition.v1.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
@Table
public class PersonPositionDevice {
	/**
	 * 设备编号
	 */
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	@Column(length=64)
	private String id;
	/**
	 * 设备编号
	 */
	private String deviceCode;
	
	/**
	 * 设备状态
	 */
	private String deviceState;
	
	/**
	 * 设备类型
	 */
	private String deviceType;
	
	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@CreatedDate
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
