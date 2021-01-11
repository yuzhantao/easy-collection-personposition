package com.bimuo.easy.collection.personposition.v1.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.service.IPersonPositionDeviceService;
import com.bimuo.easy.collection.personposition.v1.service.vo.BrandInfo;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 设备历史信息控制器
 * 
 * @author Pingfan
 *
 */
@RestController
@RequestMapping("/devices-history")
public class DeviceHistoryController {
	private static final Logger log = LogManager.getLogger(DeviceHistoryController.class);
	
	@Autowired
	private IPersonPositionDeviceService personPositionDeviceService;
	
	/**
	 * 根据开始结束时间查询历史设备列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param pageable 页码
	 * @return 历史设备列表
	 * @throws Exception
	 */
	@GetMapping
	public Page<PersonPositionDevice> queryDeviceHistoryList(
			@RequestParam(required=false) 
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
			Date startTime,
			@RequestParam(required=false) 
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
			Date endTime,
			@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		return personPositionDeviceService.queryHistory(startTime,endTime,pageable);
	}
	
	/**
	 * 历史数据导出Excel表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param response 返回响应
	 * @throws Exception
	 */
	@GetMapping("/exportExcel")
	public void exportExcel(
			@RequestParam(required=false) 
			@DateTimeFormat(pattern = "yyyy-MM-dd")
			Date startTime,
			@RequestParam(required=false) 
			@DateTimeFormat(pattern = "yyyy-MM-dd")
			Date endTime,
			HttpServletResponse response) throws Exception {
		log.info("Excel 导出开始......");
		// 获取用户信息
		List<BrandInfo> list = personPositionDeviceService.toExcel(startTime,endTime);
		try {
			// 设置响应输出的头类型及下载文件的默认名称
			String fileName = new String("设备历史信息表.xls".getBytes("utf-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			// 导出
			Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), BrandInfo.class, list);
			workbook.write(response.getOutputStream());
			log.info("Excel 导出成功!");
		} catch (IOException e) {
			log.info("Excel 导出异常：{}", e.getMessage());
		}
	}
}
