package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签252类型协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag252Vo extends DeviceTagReadVo {
	
	private int tagId; // 标签ID,11位
	private String button; // 按钮状态,1位(0按钮按下,1按钮没按下)
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private String sign; // 温度范围,1位(0温度值为零上,1温度值为零下)
	private float temp; // 温度值,10位.该字段表示无符号整数。实际温度=Temp/10，故实际湿度精确到0.1摄氏度
	
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	@Override
	public int getTagType() {
		return 252;
	}
	
}
