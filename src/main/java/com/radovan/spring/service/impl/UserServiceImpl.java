package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserById(Integer id) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findById(id)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The user has not been found")));
		return tempConverter.userEntityToDto(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDto> listAllUsers() {
		// TODO Auto-generated method stub
		List<UserEntity> allUsers = userRepository.findAll();
		return allUsers.stream().map(tempConverter::userEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getCurrentUser() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return getUserById(authUser.getId());
	}

	@Override
	@Transactional
	public void suspendUser(Integer userId) {
		// TODO Auto-generated method stub
		UserDto user = getUserById(userId);
		user.setEnabled((short) 0);
		userRepository.saveAndFlush(tempConverter.userDtoToEntity(user));
	}

	@Override
	@Transactional
	public void clearSuspension(Integer userId) {
		// TODO Auto-generated method stub
		UserDto user = getUserById(userId);
		user.setEnabled((short) 1);
		userRepository.saveAndFlush(tempConverter.userDtoToEntity(user));
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean isAdmin() {
		// TODO Auto-generated method stub
		Boolean returnValue = false;
		UserDto authUser = getCurrentUser();
		RoleEntity roleEntity = roleRepository.findByRole("ADMIN").orElse(null);
		if (roleEntity != null) {
			if (authUser.getRolesIds().contains(roleEntity.getId())) {
				returnValue = true;
			}
		}

		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findByEmail(email)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The user has not been found")));
		return tempConverter.userEntityToDto(userEntity);
	}

}