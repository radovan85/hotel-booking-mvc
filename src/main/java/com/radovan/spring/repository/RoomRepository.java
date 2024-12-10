package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

	Optional<RoomEntity> findByRoomNumber(Integer roomNumber);
}
