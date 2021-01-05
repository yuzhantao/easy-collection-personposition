package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签6类型通用协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag6Vo extends DeviceTagReadVo {

	private int tagID; // 3字节全部表示标签ID，不含任何特征位

	public int getTagID() {
		return tagID;
	}

	public void setTagID(int tagID) {
		this.tagID = tagID;
	}
	
	@Override
	public int getTagType() {
		return 6;
	}

}
