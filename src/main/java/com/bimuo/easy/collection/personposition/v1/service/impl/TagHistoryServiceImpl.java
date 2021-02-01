package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.TagAddIdNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.TagIdAlreadyExistsException;
import com.bimuo.easy.collection.personposition.v1.model.TagHistory;
import com.bimuo.easy.collection.personposition.v1.repository.ITagHistoryRepository;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.bimuo.easy.collection.personposition.v1.service.vo.TagHistoryToExcel;
import com.google.common.base.Preconditions;

@Service
public class TagHistoryServiceImpl implements ITagHistoryService {
	private final static Logger log = LogManager.getLogger(PersonPositionDeviceServiceImpl.class);
	
	@Autowired
	private ITagHistoryRepository tagHistoryRepository;
	
	@Override
	public TagHistory getOneByTagId(String tagId) {
		TagHistory tagHistory = this.tagHistoryRepository.getOneByTagId(tagId);
		return tagHistory;
	}

	@Override
	public void save(TagHistory tagHistory) {
		this.tagHistoryRepository.save(tagHistory);
	}

	@Override
	public boolean insert(TagHistory tagHistory) {
		Preconditions.checkNotNull(tagHistory.getTagId(), new TagAddIdNoneException());
		TagHistory tag = this.tagHistoryRepository.getOneByTagId(tagHistory.getTagId());
		AssertUtils.checkArgument(tag == null, new TagIdAlreadyExistsException());
		this.tagHistoryRepository.save(tagHistory);
		log.info("添加新标签成功,标签id={}",tagHistory.getTagId());
		return true;
	}

	@Override
	public Page<TagHistory> queryTagHistory(String[] deviceCode, Date startTime, Date endTime, Pageable pageable) {
		Specification<TagHistory> specification = new Specification<TagHistory>() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public Predicate toPredicate(Root<TagHistory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                // 判定设备编号 开始时间 结束时间是否为空
                if(StringUtils.isNoneBlank(deviceCode)) {
                	Path<String> path = root.get("deviceCode");
					CriteriaBuilder.In<String> in = cb.in(path);
					for (String devCode:deviceCode){
						in.value(devCode);
					}
					predicates.add(cb.and(in));
                }
                if (startTime != null && endTime != null) {
                    //大于或等于开始时间 小于或等于结束时间
                    predicates.add(cb.between(root.get("createTime"), startTime, endTime));
                } else if (startTime != null) {
                    //大于或等于开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), startTime));
                } else if (endTime != null) {
                    //小于或等于结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), endTime));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<TagHistory> pages = tagHistoryRepository.findAll(specification,pageable);
		return pages;
	}

	@Override
	public List<TagHistory> queryHistoryList(String[] deviceCode, Date startTime, Date endTime) {
		Specification<TagHistory> specification = new Specification<TagHistory>() {
			private static final long serialVersionUID = 1L;
			
			@Override
            public Predicate toPredicate(Root<TagHistory> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                 // 判定设备编号 开始时间 结束时间是否为空
                if(StringUtils.isNoneBlank(deviceCode)) {
                	Path<String> path = root.get("deviceCode");
					CriteriaBuilder.In<String> in = cb.in(path);
					for (String devCode:deviceCode){
						in.value(devCode);
					}
					predicates.add(cb.and(in));
				}
                if (startTime != null && endTime != null) {
                    //大于或等于开始时间 小于或等于结束时间
                    predicates.add(cb.between(root.get("createTime"), startTime, endTime));
                } else if (startTime != null) {
                    //大于或等于开始时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(Date.class), startTime));
                } else if (endTime != null) {
                    //小于或等于结束时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(Date.class), endTime));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<TagHistory> tags = tagHistoryRepository.findAll(specification);
		return tags;
	}

	@Override
	public List<TagHistoryToExcel> toExcel(String[] deviceCode, Date startTime, Date endTime) {
		List<TagHistory> tags = queryHistoryList(deviceCode,startTime, endTime);
		List<TagHistoryToExcel> tagExcelInfo = new ArrayList<>(); 
		for(int i = 0; i < tags.size(); i++){ // 通过循环来赋值给另一个List
			TagHistoryToExcel tagExcel = new TagHistoryToExcel();
			tagExcel.setCreateTime(tags.get(i).getCreateTime());
			tagExcel.setDeviceCode(tags.get(i).getDeviceCode());
			tagExcel.setTagId(tags.get(i).getTagId());
			tagExcelInfo.add(tagExcel);
		}
		return tagExcelInfo;
	}

	
}
