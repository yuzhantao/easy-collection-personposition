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
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@GeneratedValue(generator = "system-uuid")
	@Column(length=64)
	private String id;
	
	private String tagId;
	
	private String deviceCode;
	
	@CreatedDate
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	
	private byte tagType;
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
	public byte getTagType() {
		return tagType;
	}
	public void setTagType(byte tagType) {
		this.tagType = tagType;
	}
	public Map<String, Object> getTagExtendParams() {
		return tagExtendParams;
	}
	public void setTagExtendParams(Map<String, Object> tagExtendParams) {
		this.tagExtendParams = tagExtendParams;
	}
	
}
