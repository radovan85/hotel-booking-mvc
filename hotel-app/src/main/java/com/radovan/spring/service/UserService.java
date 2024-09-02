package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.UserDto;

public interface UserService {

	UserDto getUserById(Integer id);

	List<UserDto> listAllUsers();

	UserDto getUserByEmail(String email);

	UserDto getCurrentUser();

	void suspendUser(Integer userId);

	void clearSuspension(Integer userId);

	Boolean isAdmin();

}
