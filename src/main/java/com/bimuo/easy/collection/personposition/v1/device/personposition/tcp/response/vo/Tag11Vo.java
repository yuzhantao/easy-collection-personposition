package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签11类型协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag11Vo extends DeviceTagReadVo {

	private int tagId;	// 标签ID,20位
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private String button; //按钮状态,1位(0按钮按下,1按钮没按下)
	private String gain; // 增益,2位(00表示增益0,01表示增益1,10表示增益2,11表示增益3)
	
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
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
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}

	@Override
	public int getTagType() {
		return 11;
	}
	
}
