package com.radovan.spring.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer reservationId;

	private Integer roomId;

	private Integer guestId;

	private String checkInDateStr;

	private String checkOutDateStr;

	private String createdAtStr;

	private String updatedAtStr;

	private Float price;

	private Integer numberOfNights;

	public Integer getReservationId() {
		return reservationId;
	}

	public void setReservationId(Integer reservationId) {
		this.reservationId = reservationId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getGuestId() {
		return guestId;
	}

	public void setGuestId(Integer guestId) {
		this.guestId = guestId;
	}

	public String getCheckInDateStr() {
		return checkInDateStr;
	}

	public void setCheckInDateStr(String checkInDateStr) {
		this.checkInDateStr = checkInDateStr;
	}

	public String getCheckOutDateStr() {
		return checkOutDateStr;
	}

	public void setCheckOutDateStr(String checkOutDateStr) {
		this.checkOutDateStr = checkOutDateStr;
	}

	public String getCreatedAtStr() {
		return createdAtStr;
	}

	public void setCreatedAtStr(String createdAtStr) {
		this.createdAtStr = createdAtStr;
	}

	public String getUpdatedAtStr() {
		return updatedAtStr;
	}

	public void setUpdatedAtStr(String updatedAtStr) {
		this.updatedAtStr = updatedAtStr;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getNumberOfNights() {
		return numberOfNights;
	}

	public void setNumberOfNights(Integer numberOfNights) {
		this.numberOfNights = numberOfNights;
	}

	public Boolean possibleCancel() {
		Boolean returnValue = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		ZonedDateTime currentTime = Instant.now().atZone(ZoneId.of("UTC"));
		LocalDateTime currentTimeLocal = currentTime.toLocalDateTime();
		LocalDateTime cancelDate = currentTimeLocal.plusDays(1);
		ZonedDateTime checkInDateTime = LocalDateTime.parse(checkInDateStr, formatter).atZone(ZoneId.of("UTC"));

		if (cancelDate.isBefore(checkInDateTime.toLocalDateTime())) {
			returnValue = true;
		}

		return returnValue;
	}

}
