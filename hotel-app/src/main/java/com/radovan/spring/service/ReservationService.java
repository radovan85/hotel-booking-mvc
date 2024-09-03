package com.radovan.spring.service;

import java.sql.Timestamp;
import java.util.List;

import com.radovan.spring.dto.ReservationDto;

public interface ReservationService {

	ReservationDto addReservation(ReservationDto reservation, Integer roomId);

	ReservationDto getReservationById(Integer reservationId);

	void deleteReservation(Integer reservationId);

	List<ReservationDto> listAll();

	List<ReservationDto> listAllByGuestId(Integer guestId);

	List<ReservationDto> listAllByRoomId(Integer roomId);

	List<ReservationDto> listAllActive();

	List<ReservationDto> listAllExpired();

	Boolean isAvailable(Integer roomId, Timestamp checkInDate, Timestamp checkOutDate);

	ReservationDto updateReservation(ReservationDto reservation, Integer reservationId);
	
	void deleteAllByRoomId(Integer roomId);
}
