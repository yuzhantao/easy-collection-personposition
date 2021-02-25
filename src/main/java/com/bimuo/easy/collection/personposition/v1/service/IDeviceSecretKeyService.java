package com.bimuo.easy.collection.personposition.v1.service;

/**
 * 密钥相关服务
 * 
 * @author Pingfan
 *
 */
public interface IDeviceSecretKeyService {

	/**
	 * 修改硬件密钥1
	 * 
	 * @param deviceId     设备编号
	 * @param oldPassword  旧密钥   (更改密码的密码：6R3SW6H8,出厂密钥初始设置：密钥1为66333356,密钥2为77552257,密钥3为33992258)
	 * @param newPassword  新密钥
	 */
	public void updateHardwareSecretKey1(String deviceId, String oldPassword, String newPassword);
	
	/**
	 * 修改硬件密钥2
	 * 
	 * @param deviceId     设备编号
	 * @param oldPassword  旧密钥   (更改密码的密码：6R3SW6H8,出厂密钥初始设置：密钥1为66333356,密钥2为77552257,密钥3为33992258)
	 * @param newPassword  新密钥
	 */
	public void updateHardwareSecretKey2(String deviceId, String oldPassword, String newPassword);
	
	/**
	 * 修改硬件密钥3
	 * 
	 * @param deviceId     设备编号
	 * @param oldPassword  旧密钥   (更改密码的密码：6R3SW6H8,出厂密钥初始设置：密钥1为66333356,密钥2为77552257,密钥3为33992258)
	 * @param newPassword  新密钥
	 */
	public void updateHardwareSecretKey3(String deviceId, String oldPassword, String newPassword);
}
