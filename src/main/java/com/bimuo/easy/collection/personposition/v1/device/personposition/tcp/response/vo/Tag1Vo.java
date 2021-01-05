package com.bimuo.easy.collection.personposition.v1.device.personposition.tcp.response.vo;

/**
 * 标签1类型协议(3字节)
 * 
 * @author Pingfan
 *
 */
public class Tag1Vo extends DeviceTagReadVo {

	private String tagId;	// 标签ID,20位	
	private String gain;	// 增益,2位 (00表示增益0,01表示增益1,10表示增益2,11表示增益3)
	
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	
	@Override
	public int getTagType() {
		return 1;
	}
}
