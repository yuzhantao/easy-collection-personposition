package com.bimuo.easy.collection.personposition.v1.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

/**
 * 人员定位设备接口测试
 * @author yuzhantao
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyCollectionPersonpositionApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class TestPersonPositionDeviceController {
	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	/**
	 * 测试新增设备
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Test
	@Transactional
    @Rollback(true)
	public void testAddDevice() throws Exception {
		PersonPositionDevice dev = new PersonPositionDevice();
		dev.setDeviceCode("000001");
		dev.setId("127.0.0.1");
		String json = JSON.toJSONString(dev);
		MvcResult mr = mockMvc.perform(
				MockMvcRequestBuilders.post("/devices/person-position-devices").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn();
		String retJson = mr.getResponse().getContentAsString();
		PersonPositionDevice ret = JSON.parseObject(retJson, PersonPositionDevice.class);
        Assert.assertNotNull(ret.getId());
	}
}
