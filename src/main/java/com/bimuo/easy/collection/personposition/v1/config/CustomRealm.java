package com.bimuo.easy.collection.personposition.v1.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomRealm extends AuthorizingRealm {
	private String loginName, loginPassword;

	public CustomRealm(String loginName, String loginPassword) {
		this.loginName = loginName;
		this.loginPassword = loginPassword;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        String username = (String) SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> stringSet = new HashSet<>();
		stringSet.add("user:show");
		stringSet.add("user:admin");
		info.setStringPermissions(stringSet);
		return info;
	}

	/**
	 * 这里可以注入userService,为了方便演示，我就写死了帐号了密码 private UserService userService;
	 * <p>
	 * 获取即将需要认证的信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		System.out.println("-------身份认证方法--------");
		String userName = (String) authenticationToken.getPrincipal();
		String userPwd = new String((char[]) authenticationToken.getCredentials());

		// 根据用户名从数据库获取密码
		if (userName == null) {
			throw new AccountException("用户名不正确");
		} else if (userName.equals(this.loginName)==false || userPwd.equals(this.loginPassword )==false) {
			throw new AccountException("用户名或密码不正确");
		}
		return new SimpleAuthenticationInfo(this.loginName, this.loginPassword, getName());
	}
}
