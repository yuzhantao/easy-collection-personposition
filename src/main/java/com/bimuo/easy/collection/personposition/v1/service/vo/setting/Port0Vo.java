package com.bimuo.easy.collection.personposition.v1.service.vo.setting;

import java.io.Serializable;

/**
 * 端口0配置实体类
 * 
 * @author Pingfan
 *
 */
public class Port0Vo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int portType;   // 自定义属性,用于修改端口时获得端口号区分二级指令INS
	private String socket0DIP; // 4字节,目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	private short DPort; // 2字节,目标端口,默认值为32500
	private short SPort; // 2字节,设备服务端口,默认值为32100
	private String mode; // 1字节,工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	private String enable; // 1字节,使能位,0:端口禁用,1:端口打开
	
	public int getPortType() {
		return portType;
	}
	public void setPortType(int portType) {
		this.portType = portType;
	}
	public String getSocket0DIP() {
		return socket0DIP;
	}
	public void setSocket0DIP(String socket0dip) {
		socket0DIP = socket0dip;
	}
	public short getDPort() {
		return DPort;
	}
	public void setDPort(short dPort) {
		DPort = dPort;
	}
	public short getSPort() {
		return SPort;
	}
	public void setSPort(short sPort) {
		SPort = sPort;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
