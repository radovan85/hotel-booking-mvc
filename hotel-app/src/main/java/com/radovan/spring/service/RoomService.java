package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.RoomDto;

public interface RoomService {

	RoomDto addRoom(RoomDto room);

	RoomDto getRoomById(Integer roomId);

	void deleteRoom(Integer roomId);

	List<RoomDto> listAll();

	List<RoomDto> listAllByCategoryId(Integer categoryId);
	
	void deleteAllByCategoryId(Integer categoryId);
}
