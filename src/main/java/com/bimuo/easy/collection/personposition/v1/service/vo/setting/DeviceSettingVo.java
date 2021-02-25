package com.bimuo.easy.collection.personposition.v1.service.vo.setting;

import java.io.Serializable;

/**
 * 硬件配置父类(含设备配置/网络参数/端口配置)
 * 
 * @author Pingfan
 *
 */
public class DeviceSettingVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DeviceBaseConfigVo baseConfig;  // 设备配置
	private NetworkParamsVo networkParams;  // 网络参数
	private Port0Vo port0;                  // 端口0
	private Port1Vo port1;                  // 端口1
//	private Port2Vo port2;                  // 端口2
//	private Port3Vo port3;                  // 端口3
	
	public DeviceBaseConfigVo getBaseConfig() {
		return baseConfig;
	}
	public void setBaseConfig(DeviceBaseConfigVo baseConfig) {
		this.baseConfig = baseConfig;
	}
	public NetworkParamsVo getNetworkParams() {
		return networkParams;
	}
	public void setNetworkParams(NetworkParamsVo networkParams) {
		this.networkParams = networkParams;
	}
	public Port0Vo getPort0() {
		return port0;
	}
	public void setPort0(Port0Vo port0) {
		this.port0 = port0;
	}
	public Port1Vo getPort1() {
		return port1;
	}
	public void setPort1(Port1Vo port1) {
		this.port1 = port1;
	}
}
