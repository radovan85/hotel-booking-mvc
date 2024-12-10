package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.dto.NoteDto;
import com.radovan.spring.dto.ReservationDto;
import com.radovan.spring.dto.RoomDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.NoteEntity;
import com.radovan.spring.entity.ReservationEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.NoteRepository;
import com.radovan.spring.repository.ReservationRepository;
import com.radovan.spring.repository.RoomRepository;
import com.radovan.spring.service.GuestService;
import com.radovan.spring.service.ReservationService;
import com.radovan.spring.service.RoomService;
import com.radovan.spring.service.UserService;
import com.radovan.spring.converter.TempConverter;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RoomService roomService;

	@Autowired
	private UserService userService;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private GuestService guestService;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private ZoneId zoneId = ZoneId.of("UTC");

	@Override
	public ReservationDto addReservation(ReservationDto reservation, Integer roomId) {
		RoomDto room = roomService.getRoomById(roomId);
		reservation.setPrice(room.getPrice() * reservation.getNumberOfNights());
		ZonedDateTime checkInDate = LocalDateTime.parse(reservation.getCheckInDateStr(), formatter).atZone(zoneId);
		ZonedDateTime checkOutDate = LocalDateTime.parse(reservation.getCheckOutDateStr(), formatter).atZone(zoneId);
		ReservationEntity reservationEntity = tempConverter.reservationDtoToEntity(reservation);
		reservationEntity.setCheckInDate(Timestamp.valueOf(checkInDate.toLocalDateTime()));
		reservationEntity.setCheckOutDate(Timestamp.valueOf(checkOutDate.toLocalDateTime()));
		reservationEntity.setRoom(roomRepository.findById(roomId).orElse(null));
		reservationEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp());
		reservationEntity.setUpdatedAt(tempConverter.getCurrentUTCTimestamp());
		ReservationEntity savedReservation = reservationRepository.save(reservationEntity);

		UserDto authUser = userService.getCurrentUser();
		NoteDto note = new NoteDto();
		note.setSubject("Reservation Created");
		String text = "User " + authUser.getFirstName() + " " + authUser.getLastName() + " reserved the room "
				+ savedReservation.getRoom().getRoomNumber() + ". Check-in is scheduled for "
				+ reservation.getCheckInDateStr();
		note.setText(text);
		NoteEntity noteEntity = tempConverter.noteDtoToEntity(note);
		noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp());
		noteRepository.save(noteEntity);
		return tempConverter.reservationEntityToDto(savedReservation);

	}

	@Override
	public ReservationDto getReservationById(Integer reservationId) {
		ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The reservation has not been found!")));
		return tempConverter.reservationEntityToDto(reservationEntity);
	}

	@Override
	public void deleteReservation(Integer reservationId) {
		ReservationDto reservation = getReservationById(reservationId);
		RoomDto room = roomService.getRoomById(reservation.getRoomId());
		GuestDto guest = guestService.getGuestById(reservation.getGuestId());
		UserDto reservationUser = userService.getUserById(guest.getUserId());
		NoteDto note = new NoteDto();
		note.setSubject("Reservation Canceled");
		String text = "";
		if (userService.isAdmin()) {
			text = "Reservation for user " + reservationUser.getFirstName() + " " + reservationUser.getLastName()
					+ " for room No " + room.getRoomNumber() + " scheduled for " + reservation.getCheckInDateStr()
					+ " has been cancelled by Administrator";
		} else {
			text = "Reservation for user " + reservationUser.getFirstName() + " " + reservationUser.getLastName()
					+ " for room No " + room.getRoomNumber() + " scheduled for " + reservation.getCheckInDateStr()
					+ " has been cancelled by user";
		}
		note.setText(text);
		NoteEntity noteEntity = tempConverter.noteDtoToEntity(note);
		noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp());
		noteRepository.save(noteEntity);
		reservationRepository.deleteById(reservationId);
		reservationRepository.flush();

	}

	@Override
	public List<ReservationDto> listAll() {
		List<ReservationEntity> reservations = reservationRepository.findAll();
		return reservations.stream().map(reservation -> tempConverter.reservationEntityToDto(reservation))
				.collect(Collectors.toList());
	}

	@Override
	public List<ReservationDto> listAllByGuestId(Integer guestId) {
		List<ReservationEntity> reservations = reservationRepository.findAll().stream()
				.filter(reservation -> reservation.getGuest().getGuestId().equals(guestId)).toList();
		return reservations.stream().map(reservation -> tempConverter.reservationEntityToDto(reservation))
				.collect(Collectors.toList());
	}

	@Override
	public List<ReservationDto> listAllByRoomId(Integer roomId) {
		List<ReservationEntity> reservations = reservationRepository.findAll().stream()
				.filter(reservation -> reservation.getRoom().getRoomId().equals(roomId)).toList();
		return reservations.stream().map(reservation -> tempConverter.reservationEntityToDto(reservation))
				.collect(Collectors.toList());
	}

	@Override
	public List<ReservationDto> listAllActive() {
		ZonedDateTime currentDate = tempConverter.getCurrentUTCTimestamp().toLocalDateTime().atZone(zoneId);
		return reservationRepository.findAll().stream().filter(
				reservation -> reservation.getCheckOutDate().toLocalDateTime().atZone(zoneId).isAfter(currentDate))
				.map(tempConverter::reservationEntityToDto).collect(Collectors.toList());
	}

	@Override
	public List<ReservationDto> listAllExpired() {
		ZonedDateTime currentDate = tempConverter.getCurrentUTCTimestamp().toLocalDateTime().atZone(zoneId);
		return reservationRepository.findAll().stream().filter(
				reservation -> reservation.getCheckOutDate().toLocalDateTime().atZone(zoneId).isBefore(currentDate))
				.map(tempConverter::reservationEntityToDto).collect(Collectors.toList());
	}

	@Override
	public Boolean isAvailable(Integer roomId, Timestamp checkInDate, Timestamp checkOutDate) {
		List<ReservationEntity> reservations = reservationRepository.findAll().stream()
				.filter(reservation -> reservation.getRoom().getRoomId().equals(roomId)).toList();

		for (ReservationEntity reservation : reservations) {
			if (checkInDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ReservationDto updateReservation(ReservationDto reservation, Integer reservationId) {
		ReservationEntity reservationEntity = reservationRepository.findById(reservationId).orElse(null);
		if (reservationEntity != null) {
			ReservationEntity updatedEntity = tempConverter.reservationDtoToEntity(reservation);
			updatedEntity.setReservationId(reservationEntity.getReservationId());
			updatedEntity.setCreatedAt(reservationEntity.getCreatedAt());
			updatedEntity.setUpdatedAt(tempConverter.getCurrentUTCTimestamp());
			updatedEntity.setCheckInDate(reservationEntity.getCheckInDate());
			updatedEntity.setCheckOutDate(reservationEntity.getCheckOutDate());
			ReservationEntity updatedReservation = reservationRepository.save(updatedEntity);
			NoteDto note = new NoteDto();
			note.setSubject("Reservation Updated");
			String text = "Reservation " + reservation.getReservationId() + " scheduled for "
					+ reservation.getCheckInDateStr() + " has been switched to room "
					+ updatedReservation.getRoom().getRoomNumber();
			note.setText(text);
			NoteEntity noteEntity = tempConverter.noteDtoToEntity(note);
			noteEntity.setCreatedAt(tempConverter.getCurrentUTCTimestamp());
			noteRepository.save(noteEntity);
			return tempConverter.reservationEntityToDto(updatedReservation);
		}
		return null;
	}

	@Override
	public void deleteAllByRoomId(Integer roomId) {
		List<ReservationEntity> reservations = reservationRepository.findAll().stream()
				.filter(reservation -> reservation.getRoom().getRoomId().equals(roomId)).collect(Collectors.toList());
		reservationRepository.deleteAll(reservations);
	}
}
