package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签30类型协议(7字节)
 * 
 * @author Pingfan
 *
 */
public class Tag30Vo extends DeviceTagReadVo {
	
	private long tagId;	// 标签ID,24位(3字节)
	private String activate; // 激活位置,1位(0在不激活区1在激活器)
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private String tamper;	// 防拆状态,1位(0防拆正常,1防拆异常)
	private String button1; // 按钮1状态,1位(0按钮按下,1按钮没按下)
	private String button2; // 按钮2状态,1位(0按钮按下,1按钮没按下)
	private String gain; // 增益,2位(00表示增益0,01表示增益1,10表示增益2,11表示增益3)
	private String traverse; // 穿越状态,1位(0没发生穿越,1发生穿越)
	private int activatorId; // 激活器ID,16位(2字节)	
	private int RSSI; // 场强值,8位(1字节)	
	
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public String getActivate() {
		return activate;
	}
	public void setActivate(String activate) {
		this.activate = activate;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getTamper() {
		return tamper;
	}
	public void setTamper(String tamper) {
		this.tamper = tamper;
	}
	public String getButton1() {
		return button1;
	}
	public void setButton1(String button1) {
		this.button1 = button1;
	}
	public String getButton2() {
		return button2;
	}
	public void setButton2(String button2) {
		this.button2 = button2;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	public String getTraverse() {
		return traverse;
	}
	public void setTraverse(String traverse) {
		this.traverse = traverse;
	}
	public int getActivatorId() {
		return activatorId;
	}
	public void setActivatorId(int activatorId) {
		this.activatorId = activatorId;
	}
	public int getRSSI() {
		return RSSI;
	}
	public void setRSSI(int rSSI) {
		RSSI = rSSI;
	}

	@Override
	public int getTagType() {
		return 30;
	}

}
