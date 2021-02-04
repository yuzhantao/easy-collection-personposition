package com.bimuo.easy.collection.personposition.v1.service;

import com.bimuo.easy.collection.personposition.v1.model.User;

public interface IUserService {
	User findUserByAccount(String loginName);
}
