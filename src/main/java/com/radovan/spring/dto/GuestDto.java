package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class GuestDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer guestId;

	private String phoneNumber;

	private Long idNumber;

	private Integer userId;

	private List<Integer> reservationsIds;

	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(Long idNumber) {
		this.idNumber = idNumber;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<Integer> getReservationsIds() {
		return reservationsIds;
	}

	public void setReservationsIds(List<Integer> reservationsIds) {
		this.reservationsIds = reservationsIds;
	}

}
