package com.bimuo.easy.collection.personposition.v1.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.bimuo.easy.collection.personposition.EasyCollectionPersonpositionApplication;
/**
 * 历史信息测试类
 * 
 * @author Pingfan
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyCollectionPersonpositionApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class TestDeviceHistoryController {
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	/**
	 * 测试显示历史列表
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Test
	@Transactional
	@Rollback(true)
	public void testQueryDeviceHistoryList() throws Exception {
		Date startTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = "2021-1-11";
		startTime = format.parse(str);
		String json = JSON.toJSONString(startTime);
		// TODO 怎么传时间
		MvcResult mr = mockMvc.perform(
				MockMvcRequestBuilders.get("/devices-history").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn();
		String retJson = mr.getResponse().getContentAsString();
        Assert.assertNotNull(retJson);
	}
	
	/**
	 * 测试导出Excel
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Test
	@Transactional
	@Rollback(true)
	public void testExportExcel() throws Exception {
		// TODO 怎么传参数
		MvcResult mr = mockMvc.perform(
				MockMvcRequestBuilders.get("/devices-history/exportExcel").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn();
		String retJson = mr.getResponse().getContentAsString();
        Assert.assertNotNull(retJson);
	}

}
