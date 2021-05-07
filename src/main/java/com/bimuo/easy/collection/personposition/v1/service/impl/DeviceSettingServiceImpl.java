package com.bimuo.easy.collection.personposition.v1.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message.PersonPositionMessageFactory;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceCodeNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceNetworkGetwayNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceNetworkIpNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceNetworkSourceHardwareNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceNetworkSubnetNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.exception.DeviceUpperComputerIpNotCompleteException;
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.repository.IPersonPositionDeviceRepository;
import com.bimuo.easy.collection.personposition.v1.service.IDeviceSettingService;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.DeviceSettingVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.NetworkParamsVo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port0Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port1Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port2Vo;
import com.bimuo.easy.collection.personposition.v1.service.vo.setting.Port3Vo;
import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

@Service
public class DeviceSettingServiceImpl implements IDeviceSettingService {
	private static final Logger logger = LogManager.getLogger(DeviceSettingServiceImpl.class);
	
	@Autowired
	private IPersonPositionDeviceRepository personPositionDeviceRepository;
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	private PersonPositionMessageFactory personPositionMessageFactory = new PersonPositionMessageFactory();
	
	/**
	 * 更新内存中网络参数
	 * @param networkParams  网络参数实体
	 * @param sourceIp       设备IP，默认值为192.168.1.254
	 * @param subnetMask     设备子网掩码，默认值为255.255.255.0
	 * @param gatway         设备网关，默认值为192.168.1.1
	 * @param sourceHardware 设备物理地址，默认值为00:16:24:38:65:88
	 */
	private void setNetworkParams(NetworkParamsVo networkParams, byte[] sourceIp, byte[] subnetMask, byte[] gatway, byte[] sourceHardware) {
		if (sourceIp != null && sourceIp.length != 0) {
			String sourceIpStr1 = Integer.toString(ByteUtil.byteToInt(sourceIp[0]));
			String sourceIpStr2 = Integer.toString(ByteUtil.byteToInt(sourceIp[1]));
			String sourceIpStr3 = Integer.toString(ByteUtil.byteToInt(sourceIp[2]));
			String sourceIpStr4 = Integer.toString(ByteUtil.byteToInt(sourceIp[3]));
			String sourceIpStr = sourceIpStr1 + "." + sourceIpStr2 + "." + sourceIpStr3 + "." + sourceIpStr4;
			networkParams.setSourceIp(sourceIpStr);
		} else {
			new DeviceNetworkIpNotCompleteException();
		}
		if (subnetMask != null && subnetMask.length != 0) {
			String subnetMaskStr1 = Integer.toString(ByteUtil.byteToInt(subnetMask[0]));
			String subnetMaskStr2 = Integer.toString(ByteUtil.byteToInt(subnetMask[1]));
			String subnetMaskStr3 = Integer.toString(ByteUtil.byteToInt(subnetMask[2]));
			String subnetMaskStr4 = Integer.toString(ByteUtil.byteToInt(subnetMask[3]));
			String subnetMaskStr = subnetMaskStr1 + "." + subnetMaskStr2 + "." + subnetMaskStr3 + "." + subnetMaskStr4;
			networkParams.setSubnetMask(subnetMaskStr);
		} else {
			new DeviceNetworkSubnetNotCompleteException();
		}
		if (gatway != null && gatway.length != 0) {
			String gatwayStr1 = Integer.toString(ByteUtil.byteToInt(gatway[0]));
			String gatwayStr2 = Integer.toString(ByteUtil.byteToInt(gatway[1]));
			String gatwayStr3 = Integer.toString(ByteUtil.byteToInt(gatway[2]));
			String gatwayStr4 = Integer.toString(ByteUtil.byteToInt(gatway[3]));
			String gatwayStr = gatwayStr1 + "." + gatwayStr2 + "." + gatwayStr3 + "." + gatwayStr4;
			networkParams.setGatway(gatwayStr);
		} else {
			new DeviceNetworkGetwayNotCompleteException();
		}
		if (sourceHardware != null && sourceHardware.length != 0) {
			String sourceHardwareStr1 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[0]));
			// TODO 需要调整显示00
			if(sourceHardwareStr1.equals("0")) sourceHardwareStr1 = "00";
			String sourceHardwareStr2 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[1]));
			String sourceHardwareStr3 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[2]));
			String sourceHardwareStr4 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[3]));
			String sourceHardwareStr5 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[4]));
			String sourceHardwareStr6 = Integer.toHexString(ByteUtil.byteToInt(sourceHardware[5]));
			String sourceHardwareStr = sourceHardwareStr1 + ":" + sourceHardwareStr2 + ":" + sourceHardwareStr3 + ":" + sourceHardwareStr4 + ":" + sourceHardwareStr5 + ":" + sourceHardwareStr6;
			networkParams.setSourceHardware(sourceHardwareStr);
		} else {
			new DeviceNetworkSourceHardwareNotCompleteException();
		}
	}
	
	/**
	 * 更新内存中端口0配置
	 * @param port0                  端口0实体
	 * @param socket0dip  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 */
	private void setPort0(Port0Vo port0, byte[] socket0dip, Short DPort, Short SPort, Byte mode, Byte enable) {
		port0.setPortType(0);
		if (socket0dip != null && socket0dip.length != 0) {
			String socket0dipStr1 = Integer.toString(ByteUtil.byteToInt(socket0dip[0]));
			String socket0dipStr2 = Integer.toString(ByteUtil.byteToInt(socket0dip[1]));
			String socket0dipStr3 = Integer.toString(ByteUtil.byteToInt(socket0dip[2]));
			String socket0dipStr4 = Integer.toString(ByteUtil.byteToInt(socket0dip[3]));
			String socket0dipStr = socket0dipStr1 + "." + socket0dipStr2 + "." + socket0dipStr3 + "." + socket0dipStr4;
			port0.setSocket0DIP(socket0dipStr);
		} else {
			new DeviceUpperComputerIpNotCompleteException();
		}
		if (DPort != null) {
			port0.setDPort(DPort);
		}
		if (SPort != null) {
			port0.setSPort(SPort);
		}
		if (mode != null) {
			if(mode == 0) {
				port0.setMode("TCPServer");
			}
			if(mode == 1) {
				port0.setMode("TCPClient");
			}
			if(mode == 2) {
				port0.setMode("UDPClient");
			}
		}
		if (enable != null) {
			if(enable == 0) {
				port0.setEnable("Disable");
			}
			if(enable == 1) {
				port0.setEnable("Enable");
			}
		}
	}
	
	/**
	 * 更新内存中端口1配置
	 * @param port1                  端口1实体
	 * @param socket0dip  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 */
	private void setPort1(Port1Vo port1, byte[] socket0dip, Short DPort, Short SPort, Byte mode, Byte enable) {
		port1.setPortType(1);
		if (socket0dip != null && socket0dip.length != 0) {
			String socket0dipStr1 = Integer.toString(ByteUtil.byteToInt(socket0dip[0]));
			String socket0dipStr2 = Integer.toString(ByteUtil.byteToInt(socket0dip[1]));
			String socket0dipStr3 = Integer.toString(ByteUtil.byteToInt(socket0dip[2]));
			String socket0dipStr4 = Integer.toString(ByteUtil.byteToInt(socket0dip[3]));
			String socket0dipStr = socket0dipStr1 + "." + socket0dipStr2 + "." + socket0dipStr3 + "." + socket0dipStr4;
			port1.setSocket0DIP(socket0dipStr);
		} else {
			new DeviceUpperComputerIpNotCompleteException();
		}
		if ((DPort != null)) {
			port1.setDPort(DPort);
		}
		if ((SPort != null)) {
			port1.setSPort(SPort);
		}
		if (mode != null) {
			if(mode == 0) {
				port1.setMode("TCPServer");
			}
			if(mode == 1) {
				port1.setMode("TCPClient");
			}
			if(mode == 2) {
				port1.setMode("UDPClient");
			}
		}
		if (enable != null) {
			if(enable == 0) {
				port1.setEnable("Disable");
			}
			if(enable == 1) {
				port1.setEnable("Enable");
			}
		}
	}
	
	/**
	 * 更新内存中端口2配置
	 * @param port0                  端口2实体
	 * @param socket0dip  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 */
//	private void setPort2(Port2Vo port2, byte[] socket0dip, Short DPort, Short SPort, Byte mode, Byte enable) {
//		port2.setPortType(2);
//		if (socket0dip != null && socket0dip.length != 0) {
//			String socket0dipStr1 = Integer.toString(ByteUtil.byteToInt(socket0dip[0]));
//			String socket0dipStr2 = Integer.toString(ByteUtil.byteToInt(socket0dip[1]));
//			String socket0dipStr3 = Integer.toString(ByteUtil.byteToInt(socket0dip[2]));
//			String socket0dipStr4 = Integer.toString(ByteUtil.byteToInt(socket0dip[3]));
//			String socket0dipStr = socket0dipStr1 + "." + socket0dipStr2 + "." + socket0dipStr3 + "." + socket0dipStr4;
//			port2.setSocket0DIP(socket0dipStr);
//		} else {
//			new DeviceUpperComputerIpNotCompleteException();
//		}
//		if ((DPort != null)) {
//			port2.setDPort(DPort);
//		}
//		if ((SPort != null)) {
//			port2.setSPort(SPort);
//		}
//		if (mode != null) {
//			if(mode == 0) {
//				port2.setMode("TCPServer");
//			}
//			if(mode == 1) {
//				port2.setMode("TCPClient");
//			}
//			if(mode == 2) {
//				port2.setMode("UDPClient");
//			}
//		}
//		if (enable != null) {
//			if(enable == 0) {
//				port2.setEnable("Disable");
//			}
//			if(enable == 1) {
//				port2.setEnable("Enable");
//			}
//		}
//	}
	
	/**
	 * 更新内存中端口3配置
	 * @param port3                  端口3实体
	 * @param socket0dip  目标IP，即上位机IP，默认值为192.168.1.253。读写器工作后将向此IP发送数据。如果为两机对连方式，注意设置设备IP和目标IP在同一网段
	 * @param DPort       目标端口,默认值为32500
	 * @param SPort       设备服务端口,默认值为32100
	 * @param mode        工作模式,范围0~2(0:TCPServer, 1:TCPClient, 2:UDPClient)
	 * @param enable      使能位,0:端口禁用,1:端口打开
	 */
//	private void setPort3(Port3Vo port3, byte[] socket0dip, Short DPort, Short SPort, Byte mode, Byte enable) {
//		port3.setPortType(3);
//		if (socket0dip != null && socket0dip.length != 0) {
//			String socket0dipStr1 = Integer.toString(ByteUtil.byteToInt(socket0dip[0]));
//			String socket0dipStr2 = Integer.toString(ByteUtil.byteToInt(socket0dip[1]));
//			String socket0dipStr3 = Integer.toString(ByteUtil.byteToInt(socket0dip[2]));
//			String socket0dipStr4 = Integer.toString(ByteUtil.byteToInt(socket0dip[3]));
//			String socket0dipStr = socket0dipStr1 + "." + socket0dipStr2 + "." + socket0dipStr3 + "." + socket0dipStr4;
//			port3.setSocket0DIP(socket0dipStr);
//		} else {
//			new DeviceUpperComputerIpNotCompleteException();
//		}
//		if ((DPort != null)) {
//			port3.setDPort(DPort);
//		}
//		if ((SPort != null)) {
//			port3.setSPort(SPort);
//		}
//		if (mode != null) {
//			if(mode == 0) {
//				port3.setMode("TCPServer");
//			}
//			if(mode == 1) {
//				port3.setMode("TCPClient");
//			}
//			if(mode == 2) {
//				port3.setMode("UDPClient");
//			}
//		}
//		if (enable != null) {
//			if(enable == 0) {
//				port3.setEnable("Disable");
//			}
//			if(enable == 1) {
//				port3.setEnable("Enable");
//			}
//		}
//	}
	
	@Override
	public void updateHardwareNetworkParams(String deviceId, String sourceIp, String subnetMask, String gatway, String sourceHardware) {
		// Controller已判断参数全空,此处不需要
		// 发送给硬件修改端口配置的指令,参考协议46
		byte[] dataArr = new byte[19]; // data段19字节
		// 设置各参数
		dataArr[0] = 0x41; // INS为A(41)表示网络参数
		if (StringUtils.isNotBlank(sourceIp)) {
			byte[] bSourceIp = new byte[4];
			for(int i = 0; i<bSourceIp.length; i++) {
				bSourceIp[i] = ByteUtil.ipToBytesByInet(sourceIp)[i];  // ip地址转为byte数组
			}
			System.arraycopy(bSourceIp, 0, dataArr, 1, 4);      // 将转换后的byte数组赋值给指令对应位置
		}
		if (StringUtils.isNotBlank(subnetMask)) {
			byte[] bSubnetMask = new byte[4];
			for(int i = 0; i<bSubnetMask.length; i++) {
				bSubnetMask[i] = ByteUtil.ipToBytesByInet(subnetMask)[i];
			}
			System.arraycopy(bSubnetMask, 0, dataArr, 5, 4);
		}
		if (StringUtils.isNotBlank(gatway)) {
			byte[] bGatway = new byte[4];
			for(int i = 0; i<bGatway.length; i++) {
				bGatway[i] = ByteUtil.ipToBytesByInet(gatway)[i];
			}
			System.arraycopy(bGatway, 0, dataArr, 9, 4);
		}
		if (StringUtils.isNotBlank(sourceHardware)) {
			String[] sourceHardwareArr = sourceHardware.split("\\:");
			byte[] bSourceHardware = new byte[6];
			for(int i = 0; i<bSourceHardware.length; i++) {
				int value = Integer.parseInt(sourceHardwareArr[i], 16);
				bSourceHardware[i] = (byte) value;
			}
			System.arraycopy(bSourceHardware, 0, dataArr, 13, 6);
		}	
		
		byte[] command = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes(deviceId), (byte) 0x46, dataArr);
		
		logger.debug("向硬件发送的修改【网络参数】指令是{}", ByteUtil.byteArrToHexString(command, true));
		logger.info("正在向【{}】下发【修改网络参数】指令……", deviceId);
		
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(deviceId);
			if (channel == null) {
				logger.error("code-channel表中不存在设备编号【{}】的管道或该管道已被系统删除", deviceId);
			} else if (!channel.isActive() || !channel.isWritable()) {
				logger.error("设备编号【{}】的管道不可用", deviceId);
			} else {
				logger.debug("设备编号【{}】对应的管道是{}", deviceId, channel);
				ByteBuf bs = Unpooled.copiedBuffer(command);
				ChannelFuture cf = channel.writeAndFlush(bs);
				// 回调函数监听是否发送成功
				cf.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if (future.isSuccess()) {
							logger.debug("向【{}】发送修改【网络参数】命令成功,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						} else {
							logger.error("向【{}】发送修改【网络参数】命令失败,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						}
					}
				});
			}
		}
	
	@Override
	public void updateHardwarePortConfig(String deviceId, Integer portType, String socket0dip, Short DPort, Short SPort, String mode, String enable) {
		// Controller已判断参数全空,此处不需要
		// 发送给硬件修改端口配置的指令,参考协议46
		byte[] dataArr = new byte[11]; // data段11字节
		// 设置各参数
		if (portType == 0) {
			dataArr[0] = 0x42; // 1字节,二级指令,端口0等于‘B’,端口1等于‘C’,端口2等于‘D’,端口3等于‘E’
		} else if(portType == 1) {
			dataArr[0] = 0x43;
		} else if(portType == 2) {
			dataArr[0] = 0x44;
		} else if(portType == 3) {
			dataArr[0] = 0x45;
		}
		if (StringUtils.isNotBlank(socket0dip)) {
			byte[] bSocket0dip = new byte[4];
			for(int i = 0; i<bSocket0dip.length; i++) {
				bSocket0dip[i] = ByteUtil.ipToBytesByInet(socket0dip)[i];  // ip地址转为byte数组
			}
			System.arraycopy(bSocket0dip, 0, dataArr, 1, 4);      // 将转换后的byte数组赋值给指令对应位置
		}
		if (DPort != null) {
			byte[] dPortArr = ByteUtil.shortToByteArr(DPort);
			System.arraycopy(dPortArr, 0, dataArr, 5, 2);
		}
		if (SPort != null) {
			byte[] dSortArr = ByteUtil.shortToByteArr(SPort);
			System.arraycopy(dSortArr, 0, dataArr, 7, 2);
		}
		if (StringUtils.isNotBlank(mode)) {
			if(mode.equals("TCPServer")) {
				dataArr[9] = 0;
			}
			if(mode.equals("TCPClient")) {
				dataArr[9] = 1;
			}
			if(mode.equals("UDPClient")) {
				dataArr[9] = 2;
			}
		}
		if (StringUtils.isNotBlank(enable)) {
			if(enable.equals("Disable")) {
				dataArr[10] = 0;
			}
			if(enable.equals("Enable")) {
				dataArr[10] = 1;
			}
		}
		
		byte[] command = PersonPositionMessageFactory.createMessage(ByteUtil.hexStringToBytes(deviceId), (byte) 0x46, dataArr);
		
		logger.debug("向硬件发送的修改端口指令是{}", ByteUtil.byteArrToHexString(command, true));
		logger.info("正在向【{}】下发【修改端口】指令……", deviceId);
		
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(deviceId);
		if (channel == null) {
			logger.error("code-channel表中不存在设备编号【{}】的管道或该管道已被系统删除", deviceId);
		} else if (!channel.isActive() || !channel.isWritable()) {
			logger.error("设备编号【{}】的管道不可用", deviceId);
		} else {
			logger.debug("设备编号【{}】对应的管道是{}", deviceId, channel);
			ByteBuf bs = Unpooled.copiedBuffer(command);
			ChannelFuture cf = channel.writeAndFlush(bs);
			// 回调函数监听是否发送成功
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						logger.debug("向【{}】发送修改【端口】命令成功,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
					} else {
						logger.error("向【{}】发送修改【端口】命令失败,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
					}
				}
			});
		}
	}

	@Override
	public NetworkParamsVo updateNetworkParams(String deviceId, byte[] sourceIp, byte[] subnetMask, byte[] gatway, byte[] sourceHardware) {
		NetworkParamsVo networkParams = new NetworkParamsVo();
		// 判断用于查询的编号是否为空
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceId), new DeviceCodeNoneException());
		PersonPositionDevice device = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
		// 更新网络参数
		setNetworkParams(networkParams, sourceIp, subnetMask, gatway, sourceHardware);
		// 更新原先记录
		if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
			DeviceSettingVo dsetting = new DeviceSettingVo();
			dsetting.setNetworkParams(networkParams);
			device.setDeviceSetting(dsetting);
		} else {
			device.getDeviceSetting().setNetworkParams(networkParams);
		}
		personPositionDeviceService.modify(device);
		logger.debug("==========数据库【修改】设备编号【{}】网络参数成功!",device.getDeviceCode());
		return networkParams;
	}

	@Override
	public Port0Vo updatePort0(String deviceId, byte[] socket0dip, Short DPort, Short SPort, Byte mode, Byte enable) {
		Port0Vo port0 = new Port0Vo();
		// 判断用于查询的编号是否为空
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceId), new DeviceCodeNoneException());
		PersonPositionDevice device = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
		// 更新端口0配置
		setPort0(port0, socket0dip, DPort, SPort, mode, enable);
		// 更新原先记录
		if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
			DeviceSettingVo dsetting = new DeviceSettingVo();
			dsetting.setPort0(port0);
			device.setDeviceSetting(dsetting);
		} else {
			device.getDeviceSetting().setPort0(port0);
		}
		personPositionDeviceService.modify(device);
		logger.debug("==========数据库【修改】设备编号【{}】端口0成功!",device.getDeviceCode());
		return port0;
	}

	@Override
	public Port1Vo updatePort1(String deviceId, byte[] socket0dip, Short DPort, Short SPort, Byte mode,Byte enable) {
		Port1Vo port1 = new Port1Vo();
		// 判断用于查询的编号是否为空
		Preconditions.checkArgument(StringUtils.isNotBlank(deviceId), new DeviceCodeNoneException());
		PersonPositionDevice device = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
		// 更新端口1配置
		setPort1(port1, socket0dip, DPort, SPort, mode, enable);
		// 更新原先记录
		if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
			DeviceSettingVo dsetting = new DeviceSettingVo();
			dsetting.setPort1(port1);
			device.setDeviceSetting(dsetting);
		} else {
			device.getDeviceSetting().setPort1(port1);
		}
		personPositionDeviceService.modify(device);
		logger.debug("==========数据库【修改】设备编号【{}】端口1成功!",device.getDeviceCode());
		return port1;
	}

//	@Override
//	public Port2Vo updatePort2(String deviceId, byte[] socket0dip, Short DPort, Short SPort, Byte mode,Byte enable) {
//		Port2Vo port2 = new Port2Vo();
//		// 判断用于查询的编号是否为空
//		Preconditions.checkArgument(StringUtils.isNotBlank(deviceId), new DeviceCodeNoneException());
//		PersonPositionDevice device = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
//		// 更新端口2配置
//		setPort2(port2, socket0dip, DPort, SPort, mode, enable);
//		// 更新原先记录
//		if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
//			DeviceSettingVo dsetting = new DeviceSettingVo();
//			dsetting.setPort2(port2);
//			device.setDeviceSetting(dsetting);
//		} else {
//			device.getDeviceSetting().setPort2(port2);
//		}
//		personPositionDeviceService.modify(device);
//		logger.info("==========数据库【修改】设备编号【{}】端口2成功!",device.getDeviceCode());
//		return port2;
//	}

//	@Override
//	public Port3Vo updatePort3(String deviceId, byte[] socket0dip, Short DPort, Short SPort, Byte mode,Byte enable) {
//		Port3Vo port3 = new Port1Vo();
//		// 判断用于查询的编号是否为空
//		Preconditions.checkArgument(StringUtils.isNotBlank(deviceId), new DeviceCodeNoneException());
//		PersonPositionDevice device = this.personPositionDeviceRepository.getOneByDeviceCode(deviceId);
//		// 更新端口3配置
//		setPort3(port3, socket0dip, DPort, SPort, mode, enable);
//		// 更新原先记录
//		if(device.getDeviceSetting() == null) { // 重连或修改编号的新设备
//			DeviceSettingVo dsetting = new DeviceSettingVo();
//			dsetting.setPort2(port2);
//			device.setDeviceSetting(dsetting);
//		} else {
//			device.getDeviceSetting().setPort2(port2);
//		}
//		personPositionDeviceService.modify(device);
//		logger.info("==========数据库【修改】设备编号【{}】端口3成功!",device.getDeviceCode());
//		return port3;
//	}
	
	@Override
	public void resetHardware(String deviceId) {
		byte[] command = {0x02,0x03,0x04,0x05,0x00,0x13,0x00,0x58,0x61,0x00,0x4D,0x43,0x55,0x52,0x45,0x53,0x45,0x54,0x42};
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeMapping.getInstance().getChannel(deviceId);
		if (channel == null) {
			logger.error("code-channel表中不存在设备编号【{}】的管道或该管道已被系统删除", deviceId);
			//CommandStateMapping.getInstance().addStateMapping(deviceId, "NoChannel");
		} else if (!channel.isActive() || !channel.isWritable()) {
			logger.error("设备编号【{}】的管道不可用", deviceId);
			//CommandStateMapping.getInstance().addStateMapping(deviceId, "ChannelDisable");
		} else {
			logger.debug("设备编号【{}】对应的管道是{}", deviceId, channel);
			ByteBuf bs = Unpooled.copiedBuffer(command);
			ChannelFuture cf = channel.writeAndFlush(bs);
			// 回调函数监听是否发送成功
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						// TODO 存在异步问题 还未执行监听时Controller已继续执行 则不会返回任何东西
						logger.info("向【{}】发送【复位】命令成功,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						//CommandStateMapping.getInstance().addStateMapping(deviceId, "Success");
					} else {
						logger.error("向【{}】发送【复位】命令失败,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						//CommandStateMapping.getInstance().addStateMapping(deviceId, "Fail");
					}
				}
			});
		}
	}
}
