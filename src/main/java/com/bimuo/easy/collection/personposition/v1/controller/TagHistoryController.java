package com.bimuo.easy.collection.personposition.v1.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.exception.TagHistoryAddFailedException;
import com.bimuo.easy.collection.personposition.v1.model.TagHistory;
import com.bimuo.easy.collection.personposition.v1.service.ITagHistoryService;
import com.bimuo.easy.collection.personposition.v1.service.vo.TagHistoryToExcel;
import com.google.common.base.Preconditions;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 标签历史控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/tags-history")
public class TagHistoryController {
	private static final Logger log = LogManager.getLogger(DeviceHistoryController.class);
	
	@Autowired
	private ITagHistoryService tagHistoryService;
	
	/**
	 * 根据开始结束时间查询历史设备列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pageable 页码
	 * @return 历史设备列表
	 * @throws Exception
	 */
	@GetMapping("/list")
	public ResponseEntity<?> queryTagHistoryList(
			@RequestParam(required=false) 
			String[] deviceCode,
			@RequestParam(required=false) 
			String startTime,
			@RequestParam(required=false) 
			String endTime,
			@PageableDefault(value = 10, sort = { "createTime" }, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date realStartTime = null;
		Date realEndTime = null;
		if(StringUtils.isNotBlank(startTime)) {
			realStartTime = simpleDateFormat.parse(startTime);	
		}
		if(StringUtils.isNotBlank(endTime)) {
			realEndTime = simpleDateFormat.parse(endTime);
		}
		
		Page<TagHistory> page = tagHistoryService.queryTagHistory(deviceCode,realStartTime,realEndTime,pageable);
		return ResponseEntity.ok(page);
	}
	
	
	/**
	 * 添加标签接口
	 * @param tagHistory 标签实体
	 * @return 添加的标签实体
	 * @throws Exception
	 */
	@PostMapping
	public ResponseEntity<?> addTagHistory(@RequestBody TagHistory tagHistory) throws Exception {
		// TODO 参数是否和修改一样?
		// TODO 是否需要打印日志?
		tagHistory.setCreateTime(new Date());
		boolean isSuccess = tagHistoryService.insert(tagHistory);
		Preconditions.checkArgument(isSuccess,new TagHistoryAddFailedException());
		return ResponseEntity.ok(tagHistory);
	}
	
	/**
	 * 历史数据导出Excel表
	 * @param deviceCode 设备编号
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param response 返回响应
	 * @throws Exception
	 */
	@GetMapping("/exportExcel")
	public void exportExcel(
			@RequestParam(required=false) String[] deviceCode,
			@RequestParam(required=false) String startTime,
			@RequestParam(required=false) String endTime,
			HttpServletResponse response) throws Exception {
		log.info("Excel 导出开始......");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date realStartTime = null;
		Date realEndTime = null;
		if(StringUtils.isNotBlank(startTime)) {
			realStartTime = simpleDateFormat.parse(startTime);	
		}
		if(StringUtils.isNotBlank(endTime)) {
			realEndTime = simpleDateFormat.parse(endTime);
		}
		// 获取用户信息
		List<TagHistoryToExcel> list = tagHistoryService.toExcel(deviceCode, realStartTime,realEndTime);
		try {
			// 设置响应输出的头类型及下载文件的默认名称
			String fileName = new String("标签历史信息表.xls".getBytes("utf-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			// 导出
			Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), TagHistoryToExcel.class, list);
			workbook.write(response.getOutputStream());
			log.info("Excel 导出成功!");
		} catch (IOException e) {
			log.info("Excel 导出异常：{}", e.getMessage());
		}
	}
	
	@DeleteMapping
	public void clearDataBase() {
		tagHistoryService.clearTable();
	}
}
