package com.bimuo.easy.collection.personposition.v1.service;

/**
 * 密钥相关服务
 * 
 * @author Pingfan
 *
 */
public interface IDeviceSecretKeyService {

	/**
	 * 修改硬件密钥
	 * 
	 * @param deviceId     设备编号
	 * @param oldPassword  旧密钥   (出厂密钥初始设置：密钥1为66333356,密钥2为77552257,密钥3为33992258)
	 * @param newPassword  新密钥
	 */
	public void updateHardwareSecretKey(String deviceId, String oldPassword, String newPassword);
}
