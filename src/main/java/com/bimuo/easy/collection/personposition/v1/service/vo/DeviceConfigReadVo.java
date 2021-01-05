package com.bimuo.easy.collection.personposition.v1.service.vo;

/**
 * 读取设备配置实体类
 * 
 * @author Pingfan
 *
 */
public class DeviceConfigReadVo {

	private String deviceId; //设备ID
	private byte cain1; //发送增益,范围0~3
	private byte cain2; //接收增益,范围0~31
	private byte airBaudrate; //空中波特率,范围0~2(0:250K, 1:1M, 2:2M)
	private byte baudrate; //串口波特率,范围0~6(4800~115200)
	private boolean buzzType; //蜂鸣器状态,范围0~1(0:关,1:开)
	private boolean ioInput; //地感值,范围0~1(0:无地感,1:有地感)
	private byte critical; //两秒内接收到的同一个ID的次数阀值,范围0~8
	private byte filterTagTime; //同一个ID的过滤时间,单位秒,范围0~250
	private byte sendInterval; //两个韦根数据的发送间隔,单位0.1秒,范围0~250
	private byte tagType; //标签类型,范围0~255
	private boolean crcEn; //设备CRC状态,范围0~1(0:取消,1:有效)
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public byte getCain1() {
		return cain1;
	}
	public void setCain1(byte cain1) {
		this.cain1 = cain1;
	}
	public byte getCain2() {
		return cain2;
	}
	public void setCain2(byte cain2) {
		this.cain2 = cain2;
	}
	public byte getAirBaudrate() {
		return airBaudrate;
	}
	public void setAirBaudrate(byte airBaudrate) {
		this.airBaudrate = airBaudrate;
	}
	public byte getBaudrate() {
		return baudrate;
	}
	public void setBaudrate(byte baudrate) {
		this.baudrate = baudrate;
	}
	public boolean isBuzzType() {
		return buzzType;
	}
	public void setBuzzType(boolean buzzType) {
		this.buzzType = buzzType;
	}
	public boolean isIoInput() {
		return ioInput;
	}
	public void setIoInput(boolean ioInput) {
		this.ioInput = ioInput;
	}
	public byte getCritical() {
		return critical;
	}
	public void setCritical(byte critical) {
		this.critical = critical;
	}
	public byte getFilterTagTime() {
		return filterTagTime;
	}
	public void setFilterTagTime(byte filterTagTime) {
		this.filterTagTime = filterTagTime;
	}
	public byte getSendInterval() {
		return sendInterval;
	}
	public void setSendInterval(byte sendInterval) {
		this.sendInterval = sendInterval;
	}
	public byte getTagType() {
		return tagType;
	}
	public void setTagType(byte tagType) {
		this.tagType = tagType;
	}
	public boolean isCrcEn() {
		return crcEn;
	}
	public void setCrcEn(boolean crcEn) {
		this.crcEn = crcEn;
	}
	
}
