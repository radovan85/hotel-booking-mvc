package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.form.RegistrationForm;

public interface GuestService {

	GuestDto addGuest(GuestDto guest);

	GuestDto getGuestById(Integer guestId);

	GuestDto getGuestByUserId(Integer userId);

	void deleteGuest(Integer guestId);

	List<GuestDto> listAll();

	GuestDto storeGuest(RegistrationForm form);
}
