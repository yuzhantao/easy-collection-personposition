package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签10类型协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag10Vo extends DeviceTagReadVo {

	private int tagId;	// 标签ID,20位	
	private String realTagType; // 标签类型,1位(0普通标签,1温度标签)
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private String button; // 按钮状态,1位(0按钮按下,1按钮没按下)
	private String tamper; // 是否防拆,1位(0防拆正常,1防拆异常)
	
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
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getTamper() {
		return tamper;
	}
	public void setTamper(String tamper) {
		this.tamper = tamper;
	}

	@Override
	public int getTagType() {
		return 10;
	}
	
}
