package com.bimuo.easy.collection.personposition.v1.model;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;

import com.alibaba.fastjson.annotation.JSONField;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TagHistory {
	/**
	 * 数据库id
	 */
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	@Column(length=64)
	private String id;
	
	/**
	 * 标签id
	 */
	private String tagId;
	
	/**
	 * 设备编号
	 */
	private String deviceCode;
	
	/**
	 * 创建时间(设备读到标签后存到数据库的时间)
	 */
	@CreatedDate
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	/**
	 * 标签类型
	 */
	private int tagType;
	
	/**
	 * 卡扩展属性
	 */
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private Map<String,Object> tagExtendParams;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getTagType() {
		return tagType;
	}
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
	public Map<String, Object> getTagExtendParams() {
		return tagExtendParams;
	}
	public void setTagExtendParams(Map<String, Object> tagExtendParams) {
		this.tagExtendParams = tagExtendParams;
	}
	
}
