package com.bimuo.easy.collection.personposition.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bimuo.easy.collection.personposition.v1.model.PersonPositionDevice;

public interface IPersonPositionDeviceRepository extends JpaRepository<PersonPositionDevice, String>, JpaSpecificationExecutor<PersonPositionDevice> {
	PersonPositionDevice getOneByDeviceCode(String deviceCode);
}
