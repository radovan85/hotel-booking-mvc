package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.RoomCategoryDto;

public interface RoomCategoryService {

	RoomCategoryDto addCategory(RoomCategoryDto category);
	
	RoomCategoryDto getCategoryById(Integer categoryId);
	
	void deleteCategory(Integer categoryId);
	
	List<RoomCategoryDto> listAll();
}
