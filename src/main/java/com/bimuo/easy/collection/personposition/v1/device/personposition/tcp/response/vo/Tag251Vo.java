package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签251类型协议(8字节)
 * 
 * @author Pingfan
 *
 */
public class Tag251Vo extends DeviceTagReadVo {
	
	private long tagId; // 标签ID,24位(3字节)
	private String voltage; // 电压状态,1位(0电压正常,1电压低)
	private double hum; // 湿度值,16位(2字节) 该字段表示有符号整数。实际湿度(%)=(Hum/100)(%),故实际湿度精确到0.01%
	private double temp; // 温度值,16位(2字节) 该字段表示有符号整数。实际温度=Temp/100，故实际湿度精确到0.01摄氏度
	
	public long getTagId() {
		return tagId;
	}
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public double getHum() {
		return hum;
	}
	public void setHum(double hum) {
		this.hum = hum;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	@Override
	public int getTagType() {
		return 251;
	}
}
