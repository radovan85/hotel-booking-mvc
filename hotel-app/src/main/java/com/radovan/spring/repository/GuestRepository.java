package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.GuestEntity;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Integer> {

	Optional<GuestEntity> findByUserId(Integer userId);

}
