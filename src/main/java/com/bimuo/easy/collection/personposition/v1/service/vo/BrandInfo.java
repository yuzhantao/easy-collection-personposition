package com.bimuo.easy.collection.personposition.v1.service.vo;

import java.io.Serializable;

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
    private String createTime;
 
    @Excel(name = "updateTime",width = 20,exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "1")
    private String updateTime;

}
