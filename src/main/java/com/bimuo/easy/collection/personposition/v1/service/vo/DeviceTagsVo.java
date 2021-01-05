package com.bimuo.easy.collection.personposition.v1.service.vo;

import java.util.List;

public class DeviceTagsVo extends DeviceVo {
	private List<TagVo> tags;
	
	public List<TagVo> getTags() {
		return tags;
	}
	public void setTags(List<TagVo> tags) {
		this.tags = tags;
	}
}
