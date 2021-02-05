package com.bimuo.easy.collection.personposition.v1.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ShiroConfig {
	@Value("${app.default.login-name}")
	private String loginName;

	@Value("${app.default.login-password}")
	private String loginPassword;
	
	@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}

	// 将自己的验证方式加入容器
	@Bean
	public CustomRealm myShiroRealm() {
		CustomRealm customRealm = new CustomRealm(this.loginName,this.loginPassword);
		return customRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		return securityManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, Filter> filters = new HashMap<>();
        filters.put("auth", new AuthFilter());
        shiroFilterFactoryBean.setFilters(filters);
        
		Map<String, String> maps = new LinkedHashMap<>();
		// 登出
//		maps.put("/web/logout", "logout");
		maps.put("/login","anon");
		maps.put("/swagger-ui.html","anon");
		maps.put("/swagger/**","anon");
		maps.put("/v2/**","anon");
		maps.put("/swagger-resources/**","anon");
		maps.put("/webjars/**","anon");
		maps.put("/configuration/**","anon");
		maps.put("/web/user/register","anon");
		maps.put("/web/user/getAuthCode","anon");
		maps.put("/web/user/getResetPhoneAuthCode","anon");
		maps.put("/web/user/getResetPasswordAuthCode","anon");
		maps.put("/web/user/resetPassword","anon");
		maps.put("/web/user/resetPhone","anon");
		maps.put("/api/**","anon");
		
		// 对所有用户认证
		maps.put("/**", "authc");
		
		// 登录
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 首页
		shiroFilterFactoryBean.setSuccessUrl("/");
		// 错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(maps);
		return shiroFilterFactoryBean;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
