package com.bimuo.easy.collection.personposition.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;
import com.bimuo.easy.collection.personposition.v1.model.User;

public interface IUserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
	User getOneByLoginName(String loginName);
}
