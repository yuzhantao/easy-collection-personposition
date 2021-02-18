package com.bimuo.easy.collection.personposition.v1.service.vo;

import java.io.Serializable;

/**
 * 硬件网络参数实体类
 * 
 * @author Pingfan
 *
 */
public class DeviceNetworkParams implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private byte INS;        // 1字节,二级指令，等于’A’
	private int sourceIp;    // 4字节,设备IP，默认值为192.168.1.254
	private int subnetMask;  // 4字节,设备子网掩码，默认值为255.255.255.0
	private int gatway;      // 4字节,设备网关，默认值为192.168.1.1
	
	public byte getINS() {
		return INS;
	}
	public void setINS(byte iNS) {
		INS = iNS;
	}
	public int getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(int sourceIp) {
		this.sourceIp = sourceIp;
	}
	public int getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(int subnetMask) {
		this.subnetMask = subnetMask;
	}
	public int getGatway() {
		return gatway;
	}
	public void setGatway(int gatway) {
		this.gatway = gatway;
	}
	
}
