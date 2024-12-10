package com.radovan.spring.form;

import java.io.Serializable;

import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.dto.UserDto;

public class RegistrationForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserDto user;
	
	private GuestDto guest;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public GuestDto getGuest() {
		return guest;
	}

	public void setGuest(GuestDto guest) {
		this.guest = guest;
	}
	
	

}
