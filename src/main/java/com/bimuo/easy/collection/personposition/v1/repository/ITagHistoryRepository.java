package com.bimuo.easy.collection.personposition.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bimuo.easy.collection.personposition.v1.model.TagHistory;

public interface ITagHistoryRepository extends JpaRepository<TagHistory, String>, JpaSpecificationExecutor<TagHistory> {
	
	/**
	 * 根据标签编号查询一个标签
	 * @param tagId 标签编号
	 * @return 查询到的设备实体
	 */
	TagHistory getOneByTagId(String tagId);
	
	/**
	 * 只留下两天内的实时数据
	 */
	@Modifying
	@Query(value="DELETE FROM TAG_HISTORY WHERE CREATE_TIME <= DATE_FORMAT(DATE_SUB(NOW(),INTERVAL 2 DAY),'%Y-%m-%d %H:%i:%S')",nativeQuery = true)
	void clearTableMoreThanTwoDays();
}
