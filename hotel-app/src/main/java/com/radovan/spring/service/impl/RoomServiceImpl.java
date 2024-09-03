package com.radovan.spring.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radovan.spring.dto.RoomCategoryDto;
import com.radovan.spring.dto.RoomDto;
import com.radovan.spring.entity.RoomEntity;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.repository.RoomRepository;
import com.radovan.spring.service.RoomCategoryService;
import com.radovan.spring.service.RoomService;
import com.radovan.spring.converter.TempConverter;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RoomCategoryService categoryService;

	@Override
	public RoomDto addRoom(RoomDto room) {
		RoomCategoryDto roomCategory = categoryService.getCategoryById(room.getRoomCategoryId());
		Integer roomId = room.getRoomId();
		Optional<RoomEntity> existingRoomOptional = roomRepository.findByRoomNumber(room.getRoomNumber());
		if (existingRoomOptional.isPresent()) {
			if (existingRoomOptional.get().getRoomId() != roomId) {
				throw new ExistingInstanceException(new Error("This room number exists already!"));
			}
		}

		room.setPrice(roomCategory.getPrice());
		RoomEntity roomEntity = tempConverter.roomDtoToEntity(room);
		RoomEntity savedRoom = roomRepository.save(roomEntity);
		return tempConverter.roomEntityToDto(savedRoom);
	}

	@Override
	public RoomDto getRoomById(Integer roomId) {
		RoomEntity roomEntity = roomRepository.findById(roomId).orElse(null);
		return tempConverter.roomEntityToDto(roomEntity);
	}

	@Override
	public void deleteRoom(Integer roomId) {
		roomRepository.deleteById(roomId);
	}

	@Override
	public List<RoomDto> listAll() {
		List<RoomEntity> rooms = roomRepository.findAll();
		return rooms.stream().map(room -> tempConverter.roomEntityToDto(room)).collect(Collectors.toList());
	}

	@Override
	public List<RoomDto> listAllByCategoryId(Integer categoryId) {
		List<RoomEntity> rooms = roomRepository.findAll().stream()
				.filter(room -> room.getRoomCategory().getRoomCategoryId().equals(categoryId)).toList();
		return rooms.stream().map(room -> tempConverter.roomEntityToDto(room)).collect(Collectors.toList());
	}

	@Override
	public void deleteAllByCategoryId(Integer categoryId) {
		List<RoomEntity> rooms = roomRepository.findAll().stream()
				.filter(room -> room.getRoomCategory().getRoomCategoryId().equals(categoryId))
				.collect(Collectors.toList());
		roomRepository.deleteAll(rooms);
	}
}
