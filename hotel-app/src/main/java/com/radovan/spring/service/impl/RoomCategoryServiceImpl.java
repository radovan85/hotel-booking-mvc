package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.RoomCategoryDto;
import com.radovan.spring.entity.RoomCategoryEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.RoomCategoryRepository;
import com.radovan.spring.service.RoomCategoryService;

@Service
@Transactional
public class RoomCategoryServiceImpl implements RoomCategoryService {

	@Autowired
	private RoomCategoryRepository categoryRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public RoomCategoryDto addCategory(RoomCategoryDto category) {
		// TODO Auto-generated method stub
		RoomCategoryEntity categoryEntity = tempConverter.roomCategoryDtoToEntity(category);
		RoomCategoryEntity storedCategory = categoryRepository.save(categoryEntity);
		return tempConverter.roomCategoryEntityToDto(storedCategory);
	}

	@Override
	public RoomCategoryDto getCategoryById(Integer categoryId) {
		// TODO Auto-generated method stub
		RoomCategoryEntity categoryEntity = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The category has not been found!")));
		return tempConverter.roomCategoryEntityToDto(categoryEntity);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		getCategoryById(categoryId);
		categoryRepository.deleteById(categoryId);
		categoryRepository.flush();
	}

	@Override
	public List<RoomCategoryDto> listAll() {
		// TODO Auto-generated method stub
		List<RoomCategoryEntity> allCategories = categoryRepository.findAll();
		return allCategories.stream().map(category -> tempConverter.roomCategoryEntityToDto(category))
				.collect(Collectors.toList());
	}

}
