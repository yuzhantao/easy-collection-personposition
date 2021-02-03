package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签4类型通用协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag4Vo extends DeviceTagReadVo {

	private int tagId; // 3字节全部表示标签ID，不含任何特征位

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	@Override
	public int getTagType() {
		return 4;
	}

}
