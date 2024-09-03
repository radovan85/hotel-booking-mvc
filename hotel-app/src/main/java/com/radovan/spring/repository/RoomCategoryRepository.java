package com.radovan.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.RoomCategoryEntity;

@Repository
public interface RoomCategoryRepository extends JpaRepository<RoomCategoryEntity, Integer> {

}
