package com.bimuo.easy.collection.personposition.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.bimuo.easy.collection.personposition.v1.model.TagHistory;

public interface ITagHistoryRepository  extends JpaRepository<TagHistory, String>, JpaSpecificationExecutor<TagHistory> {

}
