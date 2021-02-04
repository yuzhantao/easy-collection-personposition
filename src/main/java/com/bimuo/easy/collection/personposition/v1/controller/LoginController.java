package com.bimuo.easy.collection.personposition.v1.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bimuo.easy.collection.personposition.core.util.AssertUtils;
import com.bimuo.easy.collection.personposition.v1.exception.LoginFailedException;
import com.bimuo.easy.collection.personposition.v1.exception.LoginNameEmptyException;
import com.bimuo.easy.collection.personposition.v1.exception.LoginPasswordEmptyException;
import com.bimuo.easy.collection.personposition.v1.exception.UserNotExistException;
import com.bimuo.easy.collection.personposition.v1.model.User;

@RestController
@RequestMapping("/")
public class LoginController {
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user) throws Exception {
		AssertUtils.checkArgument(StringUtils.isNoneBlank(user.getLoginName()),new LoginNameEmptyException());
		AssertUtils.checkArgument(StringUtils.isNoneBlank(user.getLoginPassword()),new LoginPasswordEmptyException());
		// 用户认证信息
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getLoginName(), user.getLoginPassword());
		try {
			// 进行验证，这里可以捕获异常，然后返回对应信息
			subject.login(usernamePasswordToken);
		} catch (UnknownAccountException e) {
			throw new UserNotExistException();
		}
		if (subject.isAuthenticated()) {
			return ResponseEntity.ok(subject.getPrincipal());
		} else {
			usernamePasswordToken.clear();
			throw new LoginFailedException();
		}
	}
}
