package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.GuestEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingInstanceException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.repository.GuestRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.GuestService;

@Service
@Transactional
public class GuestServiceImpl implements GuestService {

	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public GuestDto addGuest(GuestDto guest) {
		// TODO Auto-generated method stub
		GuestEntity guestEntity = tempConverter.guestDtoToEntity(guest);
		GuestEntity storedGuest = guestRepository.save(guestEntity);
		return tempConverter.guestEntityToDto(storedGuest);
	}

	@Override
	public GuestDto getGuestById(Integer guestId) {
		// TODO Auto-generated method stub
		GuestEntity guestEntity = guestRepository.findById(guestId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The guest has not be found!")));
		return tempConverter.guestEntityToDto(guestEntity);
	}

	@Override
	public void deleteGuest(Integer guestId) {
		// TODO Auto-generated method stub
		getGuestById(guestId);
		guestRepository.deleteById(guestId);
		guestRepository.flush();
	}

	@Override
	public List<GuestDto> listAll() {
		// TODO Auto-generated method stub
		List<GuestEntity> allGuests = guestRepository.findAll();
		return allGuests.stream().map(tempConverter::guestEntityToDto).collect(Collectors.toList());
	}

	@Override
	public GuestDto storeGuest(RegistrationForm form) {
		// TODO Auto-generated method stub

		UserDto user = form.getUser();
		Optional<UserEntity> testUserOptional = userRepository.findByEmail(user.getEmail());
		if (testUserOptional.isPresent()) {
			throw new ExistingInstanceException(new Error("Email exists"));
		}

		RoleEntity roleEntity = roleRepository.findByRole("ROLE_USER").orElse(null);
		if(roleEntity == null) {
			roleEntity = roleRepository.save(new RoleEntity("ROLE_USER"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled((short) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(roleEntity);
		UserEntity userEntity = tempConverter.userDtoToEntity(user);
		userEntity.setRoles(roles);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		roleEntity.setUsers(users);
		roleRepository.saveAndFlush(roleEntity);

		GuestDto guest = form.getGuest();
		guest.setUserId(storedUser.getId());

		GuestEntity guestEntity = tempConverter.guestDtoToEntity(guest);
		GuestEntity storedGuest = guestRepository.save(guestEntity);

		return tempConverter.guestEntityToDto(storedGuest);
	}

	@Override
	public GuestDto getGuestByUserId(Integer userId) {
		// TODO Auto-generated method stub
		GuestEntity guestEntity = guestRepository.findByUserId(userId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The guest has not be found!")));
		return tempConverter.guestEntityToDto(guestEntity);
	}

}
