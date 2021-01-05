package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签20类型协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag20Vo extends DeviceTagReadVo {

	private int tagId;	// 标签ID,17位	
	private String realTagType; // 标签类型,1位(0普通标签,1温度标签)
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private String tamper; // 防拆状态,1位(0防拆正常,1防拆异常)
	private String button1; // 按钮1状态,1位(0按钮按下,1按钮没按下)
	private String button2; // 按钮2状态,1位(0按钮按下,1按钮没按下)
	private String gain; // 增益,2位(00表示增益0,01表示增益1,10表示增益2,11表示增益3)
	
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public String getRealTagType() {
		return realTagType;
	}
	public void setRealTagType(String realTagType) {
		this.realTagType = realTagType;
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

	@Override
	public int getTagType() {
		return 20;
	}
	
}
