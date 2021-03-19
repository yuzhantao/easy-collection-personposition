package com.bimuo.easy.collection.personposition.v1.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.model.TagHistory;
import com.bimuo.easy.collection.personposition.v1.service.vo.TagHistoryToExcel;

/**
 * 标签历史服务
 * 
 * @author yuzhantao
 * @author Pingfan
 *
 */
public interface ITagHistoryService {
	
	/**
	 * 根据标签编号查询单个标签
	 * @param tagId 标签编号
	 * @return 查询的标签实体
	 */
	TagHistory getOneByTagId(String tagId);
	
	/**
	 * 保存标签数据到数据库
	 * @param tagHistory 标签历史
	 */
	void save(TagHistory tagHistory);

	/**
	 * 添加标签
	 * @param dev
	 * @return
	 */
	public boolean insert(TagHistory tagHistory);
	
	/**
	 * 查询历史标签列表
	 * @param deviceCode 设备编号
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pageable 页码
	 * @return 历史标签列表
	 */
	Page<TagHistory> queryTagHistory(String[] deviceCode, Date startTime, Date endTime, Pageable pageable);

	
	/**
	 * 根据开始结束时间不分页查询历史标签(供Excel导出用)
	 * @param deviceCode
	 * @param startTime
	 * @param endTime
	 * @return 历史标签集合
	 */
	List<TagHistory> queryHistoryList(String[] deviceCode,Date startTime,Date endTime);
	
	/**
	 * 导出两天内标签历史数据到Excel
	 * @param deviceCode 设备编号
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pageable
	 * @return Excel实体集合
	 */
	List<TagHistoryToExcel> toExcel(String[] deviceCode,Date startTime,Date endTime);
	
	/**
	 * 清除表中两天之外的数据
	 */
	void clearTable();
	
	/**
	 * 收到标签指令后向硬件回复
	 * @param deviceId 设备编号
	 * @param sn 流水号(crc正确sn=0,否则sn>0)
	 */
	void sendTagResponseToHardware(String deviceId, byte sn);
}
