package com.bimuo.easy.collection.personposition.v1.service;

import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceSettingVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.NetworkParamsVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port0Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port1Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port2Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port3Vo;

/**
 * 设备配置父类服务
 * 
 * @author Pingfan
 *
 */
public interface IDeviceSettingService {
	
	/**
	 * 修改硬件网络参数
	 * @param deviceId          设备编号
	 * @param sourceIp          设备IP,默认值为192.168.1.254
	 * @param subnetMask        设备子网掩码,默认值为255.255.255.0
	 * @param gatway            设备网关,默认值为192.168.1.1
	 * @param sourceHardware    设备物理地址,默认值为00:16:24:38:65:88
	 */
	public void updateHardwareNetworkParams(String deviceId, String sourceIp, String subnetMask, String gatway, String sourceHardware);
	
	/**
	 * 修改硬件端口配置
	 * @param deviceId    设备编号,便于根据映射表获取管道
	 * @param portType    端口类型,用于修改硬件端口时区分二级指令,端口0等于‘B’,端口1等于‘C’,端口2等于‘D’,端口3等于‘E’
	 * @param socket0DIP  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 */
	public void updateHardwarePortConfig(String deviceId, Integer portType, String socket0DIP, Short DPort, Short SPort, String mode, String enable);
	
	/**
	 * 修改数据库网络参数
	 * @param deviceId        设备编号
	 * @param sourceIp        设备IP，默认值为192.168.1.254
	 * @param subnetMask      设备子网掩码，默认值为255.255.255.0
	 * @param gatway          设备网关，默认值为192.168.1.1
	 * @param sourceHardware  设备物理地址，默认值为00:16:24:38:65:88
	 * @return 网络参数实体类
	 */
	public NetworkParamsVo updateNetworkParams(String deviceId, byte[] sourceIp, byte[] subnetMask, byte[] gatway, byte[] sourceHardware);
	
	/**
	 * 修改数据库端口0配置
	 * @param deviceId    设备编号
	 * @param socket0DIP  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 * @return 端口0实体类
	 */
	public Port0Vo updatePort0(String deviceId, byte[] socket0DIP, Short DPort, Short SPort, Byte mode, Byte enable);
	
	/**
	 * 修改数据库端口1配置
	 * @param deviceId    设备编号
	 * @param socket0DIP  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 * @return 端口1实体类
	 */
	public Port1Vo updatePort1(String deviceId, byte[] socket0DIP, Short DPort, Short SPort, Byte mode, Byte enable);
	
	/**
	 * 修改数据库端口2配置
	 * @param deviceId    设备编号
	 * @param socket0DIP  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 * @return 端口2实体类
	 */
	//public Port2Vo updatePort2(String deviceId, byte[] socket0DIP, Short DPort, Short SPort, Byte mode, Byte enable);
	
	/**
	 * 修改数据库端口3配置
	 * @param deviceId    设备编号
	 * @param socket0DIP  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 * @return 端口3实体类
	 */
	//public Port3Vo updatePort3(String deviceId, byte[] socket0DIP, Short DPort, Short SPort, Byte mode, Byte enable);
}
