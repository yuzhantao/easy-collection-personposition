package com.bimuo.easy.collection.personposition.v1.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bimuo.easy.collection.personposition.v1.model.TagHistory;

/**
 * 标签历史服务
 * @author yuzhantao
 *
 */
public interface ITagHistoryService {
	void save(TagHistory th);

	Page<TagHistory> findByCreateTime(String[] deviceCode, Date startTime, Date endTime, Pageable pageable);
}
