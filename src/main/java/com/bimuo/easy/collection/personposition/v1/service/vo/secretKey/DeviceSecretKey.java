package com.bimuo.easy.collection.personposition.v1.service.vo.secretKey;

import java.io.Serializable;

/**
 * 密钥设置父类
 * 
 * @author Pingfan
 * 
 */
public class DeviceSecretKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Key1Vo key1;
	private Key2Vo key2;
	private Key3Vo key3;
	
	public Key1Vo getKey1() {
		return key1;
	}
	public void setKey1(Key1Vo key1) {
		this.key1 = key1;
	}
	public Key2Vo getKey2() {
		return key2;
	}
	public void setKey2(Key2Vo key2) {
		this.key2 = key2;
	}
	public Key3Vo getKey3() {
		return key3;
	}
	public void setKey3(Key3Vo key3) {
		this.key3 = key3;
	}
}
