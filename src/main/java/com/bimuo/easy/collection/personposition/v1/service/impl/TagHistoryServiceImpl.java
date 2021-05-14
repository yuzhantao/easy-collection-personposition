package com.bimuo.easy.collection.personposition.v1.service.impl;

import java.text.SimpleDateFormat;
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
import org.springframework.transaction.annotation.Transactional;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.core.util.ByteUtil;
import com.bimuo.easy.collection.personposition.v1.exception.TagAddIdNoneException;
import com.bimuo.easy.collection.personposition.v1.exception.TagIdAlreadyExistsException;
import com.bimuo.easy.collection.personposition.v1.model.TagHistory;
import com.bimuo.easy.collection.personposition.v1.repository.ITagHistoryRepository;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.bimuo.easy.collection.personposition.v1.service.util.CodeChannelMapping;
import com.bimuo.easy.collection.personposition.v1.service.vo.TagHistoryToExcel;
import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

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
                criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
                criteriaQuery.orderBy(cb.desc(root.get("createTime").as(Date.class))); // 按日期降序排列
                return criteriaQuery.getRestriction();
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

	@Transactional
	@Override
	public void clearTable() {
		this.tagHistoryRepository.clearTableMoreThanTwoDays();
		log.debug("只留两天内实时数据成功!");
	}

	@Override
	public void sendTagResponseToHardware(String deviceId, byte sn) {
		byte crc = 0;
		if(sn == 0) { // sn=0表示标签crc合法
			crc = (byte) 0xB2;
		} else { // sn>0表示标签crc不合法
			crc = (byte) 0xB3;
		}
		byte[] command = {0x02, 0x03, 0x04, 0x05, 0x00, 0x0B, 0x00, 0x58, 0x41, sn, crc};
		// 根据code-channel映射表取设备对应管道
		Channel channel = CodeChannelMapping.getInstance().getChannel(deviceId);
		if (channel == null) {
			log.error("code-channel表中不存在设备编号【{}】的管道或该管道已被系统删除", deviceId);
		} else if (!channel.isActive() || !channel.isWritable()) {
			log.error("设备编号【{}】的管道不可用", deviceId);
		} else {
			log.debug("设备编号【{}】对应的管道是{}", deviceId, channel);
			ByteBuf bs = Unpooled.copiedBuffer(command);
			ChannelFuture cf = channel.writeAndFlush(bs);
			// 回调函数监听是否发送成功
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						// TODO 存在异步问题 还未执行监听时Controller已继续执行 则不会返回任何东西
						log.debug("向【{}】发送【回复标签】成功,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						//CommandStateMapping.getInstance().addStateMapping(deviceId, "Success");
					} else {
						log.debug("向【{}】发送【回复标签】失败,下发命令={}", deviceId, ByteUtil.byteArrToHexString(command, true));
						//CommandStateMapping.getInstance().addStateMapping(deviceId, "Fail");
					}
				}
			});
		}
	}
}
