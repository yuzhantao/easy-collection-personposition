package com.bimuo.easy.collection.personposition;

import java.util.UUID;

import org.junit.runner.RunWith;
import org.springframework.transaction.annotation.Transactional;

import com.bugcloud.junit.core.BugCloudAutoSpringRunner;
import com.bugcloud.junit.core.annotation.AutoTestScan;
import com.bugcloud.junit.core.annotation.PushReport;
import com.bugcloud.junit.core.annotation.RandomParameter;

@RunWith(BugCloudAutoSpringRunner.class)
@AutoTestScan(packageName = "com.bimuo.easy.collection.personposition")
@PushReport(appKey = "6585667c-6f18-4c4f-b809-3be1de3b3ca7", appSecret = "fce8d3b5-6c9b-4a49-a50b-ad519630c898", pusher = "yu", handler = "张三")
@Transactional
public class EasyCollectionPersonpositionApplicationTests {

	/**
	 * 计算接口方法中，参数名包含Id的字符型的返回值。
	 * 
	 * @return
	 */
	@RandomParameter(parameterName = ".*Id.*")
	public String userId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 计算接口方法中，参数名等于name的的返回值。
	 * 
	 * @return
	 */
	@RandomParameter(parameterName = "name")
	public String name() {
		String[] names = { "唐玄奘", "孙悟空", "猪八戒", "沙悟净" };
		return names[(int) (Math.random() * names.length)];
	}
}
