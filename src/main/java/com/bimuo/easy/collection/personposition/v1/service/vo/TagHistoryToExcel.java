package com.bimuo.easy.collection.personposition.v1.service.vo;

import java.io.Serializable;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 使用easypoi导出excel映射历史标签实体注解
 * 
 * @author Pingfan
 *
 */
@Data
public class TagHistoryToExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "创建时间",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    private Date createTime;
	
	@Excel(name = "设备编号", width = 25,orderNum = "0")
    private String deviceCode;
 
    @Excel(name = "卡号", width = 25,orderNum = "0")
    private String tagId;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
