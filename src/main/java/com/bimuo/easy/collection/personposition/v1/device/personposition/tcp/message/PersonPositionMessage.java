package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.message;

/**
 * 人员定位硬件 回复消息实体
 * 
 * @author Pingfan
 *
 */
public class PersonPositionMessage {
	/**
	 * 主机编号 2字节
	 */
	private byte[] devId;
	/**
	 * 命令 1字节 用于区分协议的类型
	 */
	private byte command;
	/**
	 * 数据包自增量 1字节 也称硬件流水号
	 */
	private byte sn;
	/**
	 * 数据 N字节 数据段
	 */
	private byte[] data;

	public PersonPositionMessage() {
		super();
	}

	public PersonPositionMessage(byte[] devId, byte command, byte sn, byte[] data) {
		super();
		this.devId = devId;
		this.command = command;
		this.sn = sn;
		this.data = data;
	}

	public byte[] getDevId() {
		return devId;
	}

	public void setDevId(byte[] devId) {
		this.devId = devId;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}
	
	public byte getSn() {
		return sn;
	}

	public void setSn(byte sn) {
		this.sn = sn;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public static String getCommandName(byte command) {
		switch (command) {
		case 100:
			return "上线";
		case -100:
			return "下线";
		case 65:
			return "读取标签指令";
		case 66:
			return "设备心跳指令";
		case 67:
			return "修改阅读器配置参数指令";
		case 68:
			return "读取阅读器配置参数指令";
		case 69:
			return "设置增益及蜂鸣器指令";
		case 70:
			return "设置阅读器网络参数指令";
		case 71:
			return "读取阅读器网络参数指令";
		case 72:
			return "声光标签批量设置参数1指令";
		case 73:
			return "声光标签批量设置参数2指令";
		case 74:
			return "可读写标签写数据指令";
		case 75:
			return "可读写标签读数据指令";
		case 76:
			return "标签防拆复位指令";
		case 77:
			return "更改验证密钥1指令";
		case 78:
			return "更改验证密钥2指令";
		case 79:
			return "更改验证密钥3指令";
		case 80:
			return "读设备版本号指令";
		case 81:
			return "读设备备注指令";
		case 82:
			return "写设备备注指令";
		case 83:
			return "标签休眠指令";
		case 84:
			return "标签参数设置指令，仅供参考";
		case 85:
			return "激活休眠全部标签指令";
		case 86:
			return "温度标签温度记录操作指令";
		case 87:
			return "读取温度标签记录时间指令";
		case 88:
			return "读取温度标签温度记录指令";
		case 89:
			return "温度标签温度校准指令";
		case 97:
			return "设备复位指令";
		case 98:
			return "设备数据传输类型设置指令";
		case 99:
			return "设备数据传输类型读取指令";
		case 102:
			return "设备数据接收地址与频道设置指令";
		case 105:
			return "继电器设置指令";
		default:
			return "未知命令";
		}
	}
}
