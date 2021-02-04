package com.bimuo.easy.collection.personposition.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.bimuo.easy.collection.personposition.v1.model.User;
import com.bimuo.easy.collection.personposition.v1.repository.IUserRepository;
import com.bimuo.easy.collection.personposition.v1.service.IUserService;

public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserRepository userRepository;
	
	@Override
	public User findUserByAccount(String loginName) {
		return userRepository.getOneByLoginName(loginName);
	}

}
