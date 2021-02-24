package com.bimuo.easy.collection.personposition.v1.service.vo.setting;

import java.io.Serializable;

/**
 * 硬件网络参数实体类
 * 
 * @author Pingfan
 *
 */
public class NetworkParamsVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String sourceIp;       // 4字节,设备IP，默认值为192.168.1.254
	private String subnetMask;     // 4字节,设备子网掩码，默认值为255.255.255.0
	private String gatway;         // 4字节,设备网关，默认值为192.168.1.1
	private String sourceHardware; // 6字节,设备物理地址，默认值为00:16:24:38:65:88
	
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public String getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
	public String getGatway() {
		return gatway;
	}
	public void setGatway(String gatway) {
		this.gatway = gatway;
	}
	public String getSourceHardware() {
		return sourceHardware;
	}
	public void setSourceHardware(String sourceHardware) {
		this.sourceHardware = sourceHardware;
	}
	
}
