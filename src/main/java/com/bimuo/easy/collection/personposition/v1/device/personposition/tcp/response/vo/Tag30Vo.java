package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签30类型协议(7字节)
 * 
 * @author Pingfan
 *
 */
public class Tag30Vo extends DeviceTagReadVo {
	
	private long tagId;	// 标签ID,24位(3字节)
	private boolean activate; // 激活位置,1位(0在不激活区1在激活器)
	private boolean voltage; // 电压状态,1位(0电压正常,1电压低)
	private boolean tamper;	// 防拆状态,1位(0防拆正常,1防拆异常)
	private boolean button1; // 按钮1状态,1位(0按钮按下,1按钮没按下)
	private boolean button2; // 按钮2状态,1位(0按钮按下,1按钮没按下)
	private String gain; // 增益,2位(00表示增益0,01表示增益1,10表示增益2,11表示增益3)
	private boolean traverse; // 穿越状态,1位(0没发生穿越,1发生穿越)
	private int activatorId; // 激活器ID,16位(2字节)	
	private int RSSI; // 场强值,8位(1字节)	
	
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public boolean isActivate() {
		return activate;
	}
	public void setActivate(boolean activate) {
		this.activate = activate;
	}
	public boolean isVoltage() {
		return voltage;
	}
	public void setVoltage(boolean voltage) {
		this.voltage = voltage;
	}
	public boolean isTamper() {
		return tamper;
	}
	public void setTamper(boolean tamper) {
		this.tamper = tamper;
	}
	public boolean isButton1() {
		return button1;
	}
	public void setButton1(boolean button1) {
		this.button1 = button1;
	}
	public boolean isButton2() {
		return button2;
	}
	public void setButton2(boolean button2) {
		this.button2 = button2;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	public boolean isTraverse() {
		return traverse;
	}
	public void setTraverse(boolean traverse) {
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
