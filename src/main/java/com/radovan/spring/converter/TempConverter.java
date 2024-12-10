package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.dto.NoteDto;
import com.radovan.spring.dto.ReservationDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.RoomCategoryDto;
import com.radovan.spring.dto.RoomDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.GuestEntity;
import com.radovan.spring.entity.NoteEntity;
import com.radovan.spring.entity.ReservationEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.RoomCategoryEntity;
import com.radovan.spring.entity.RoomEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.GuestRepository;
import com.radovan.spring.repository.ReservationRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.RoomCategoryRepository;
import com.radovan.spring.repository.RoomRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class TempConverter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoomCategoryRepository roomCategoryRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ModelMapper mapper;

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private DecimalFormat decfor = new DecimalFormat("0.00");

	private ZoneId zoneId = ZoneId.of("UTC");

	public GuestDto guestEntityToDto(GuestEntity guestEntity) {
		GuestDto returnValue = mapper.map(guestEntity, GuestDto.class);
		Optional<UserEntity> userOpt = Optional.ofNullable(guestEntity.getUser());
		userOpt.ifPresent(userEntity -> returnValue.setUserId(userEntity.getId()));

		Optional<List<ReservationEntity>> reservetationsOptional = Optional.ofNullable(guestEntity.getReservations());
		List<Integer> reservationsIds = new ArrayList<Integer>();
		if (!reservetationsOptional.isEmpty()) {
			reservetationsOptional.get().forEach((reservation) -> {
				reservationsIds.add(reservation.getReservationId());
			});
		}

		returnValue.setReservationsIds(reservationsIds);
		return returnValue;
	}

	public GuestEntity guestDtoToEntity(GuestDto guest) {
		GuestEntity returnValue = mapper.map(guest, GuestEntity.class);
		Optional<Integer> userIdOpt = Optional.ofNullable(guest.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			userRepository.findById(userId).ifPresent(returnValue::setUser);

		}

		Optional<List<Integer>> reservationsIdsOptional = Optional.ofNullable(guest.getReservationsIds());
		List<ReservationEntity> reservations = new ArrayList<ReservationEntity>();
		if (!reservationsIdsOptional.isEmpty()) {
			reservationsIdsOptional.get().forEach((reservationId) -> {
				reservationRepository.findById(reservationId).ifPresent(reservations::add);
			});
		}

		returnValue.setReservations(reservations);
		return returnValue;
	}

	public RoomDto roomEntityToDto(RoomEntity roomEntity) {
		RoomDto returnValue = mapper.map(roomEntity, RoomDto.class);
		Optional<RoomCategoryEntity> categoryOptional = Optional.ofNullable(roomEntity.getRoomCategory());
		categoryOptional.ifPresent(roomCategoryEntity -> returnValue.setRoomCategoryId(roomCategoryEntity.getRoomCategoryId()));

		Optional<List<ReservationEntity>> reservetationsOptional = Optional.ofNullable(roomEntity.getReservations());
		List<Integer> reservationsIds = new ArrayList<Integer>();
		if (!reservetationsOptional.isEmpty()) {
			reservetationsOptional.get().forEach((reservation) -> {
				reservationsIds.add(reservation.getReservationId());
			});
		}

		returnValue.setReservationsIds(reservationsIds);
		return returnValue;
	}

	public RoomEntity roomDtoToEntity(RoomDto room) {
		RoomEntity returnValue = mapper.map(room, RoomEntity.class);
		Optional<Integer> categoryIdOpt = Optional.ofNullable(room.getRoomCategoryId());
		if (categoryIdOpt.isPresent()) {
			Integer categoryId = categoryIdOpt.get();
			roomCategoryRepository.findById(categoryId).ifPresent(returnValue::setRoomCategory);
		}

		Optional<List<Integer>> reservationsIdsOptional = Optional.ofNullable(room.getReservationsIds());
		List<ReservationEntity> reservations = new ArrayList<ReservationEntity>();
		if (!reservationsIdsOptional.isEmpty()) {
			reservationsIdsOptional.get().forEach((reservationId) -> {
				reservationRepository.findById(reservationId).ifPresent(reservations::add);
			});
		}

		returnValue.setReservations(reservations);
		return returnValue;
	}

	public RoomCategoryDto roomCategoryEntityToDto(RoomCategoryEntity categoryEntity) {
		RoomCategoryDto returnValue = mapper.map(categoryEntity, RoomCategoryDto.class);

		Optional<Byte> wifiOptional = Optional.ofNullable(categoryEntity.getWifi());
		wifiOptional.ifPresent(aByte -> returnValue.setWifi(aByte.shortValue()));

		Optional<Byte> wcOptional = Optional.ofNullable(categoryEntity.getWc());
		wcOptional.ifPresent(aByte -> returnValue.setWc(aByte.shortValue()));

		Optional<Byte> tvOptional = Optional.ofNullable(categoryEntity.getTv());
		tvOptional.ifPresent(aByte -> returnValue.setTv(aByte.shortValue()));

		Optional<Byte> barOptional = Optional.ofNullable(categoryEntity.getBar());
		barOptional.ifPresent(aByte -> returnValue.setBar(aByte.shortValue()));

		Optional<List<RoomEntity>> roomsOptional = Optional.ofNullable(categoryEntity.getRooms());
		List<Integer> roomsIds = new ArrayList<>();
		if (!roomsOptional.isEmpty()) {
			roomsOptional.get().forEach((roomEntity) -> {
				roomsIds.add(roomEntity.getRoomId());
			});
		}

		returnValue.setRoomsIds(roomsIds);

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public RoomCategoryEntity roomCategoryDtoToEntity(RoomCategoryDto category) {
		RoomCategoryEntity returnValue = mapper.map(category, RoomCategoryEntity.class);

		Optional<Short> wifiOptional = Optional.ofNullable(category.getWifi());
		wifiOptional.ifPresent(aShort -> returnValue.setWifi(aShort.byteValue()));

		Optional<Short> wcOptional = Optional.ofNullable(category.getWc());
		wcOptional.ifPresent(aShort -> returnValue.setWc(aShort.byteValue()));

		Optional<Short> tvOptional = Optional.ofNullable(category.getTv());
		tvOptional.ifPresent(aShort -> returnValue.setTv(aShort.byteValue()));

		Optional<Short> barOptional = Optional.ofNullable(category.getBar());
		barOptional.ifPresent(aShort -> returnValue.setBar(aShort.byteValue()));

		Optional<List<Integer>> roomsIdsOptional = Optional.ofNullable(category.getRoomsIds());
		List<RoomEntity> rooms = new ArrayList<>();
		if (!roomsIdsOptional.isEmpty()) {
			roomsIdsOptional.get().forEach((roomId) -> {
				roomRepository.findById(roomId).ifPresent(rooms::add);
			});
		}

		returnValue.setRooms(rooms);
		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public ReservationDto reservationEntityToDto(ReservationEntity reservation) {
		ReservationDto returnValue = mapper.map(reservation, ReservationDto.class);

		Optional<RoomEntity> roomOptional = Optional.ofNullable(reservation.getRoom());
		roomOptional.ifPresent(roomEntity -> returnValue.setRoomId(roomEntity.getRoomId()));

		Optional<GuestEntity> guestOptional = Optional.ofNullable(reservation.getGuest());
		guestOptional.ifPresent(guestEntity -> returnValue.setGuestId(guestEntity.getGuestId()));

		Optional<Timestamp> checkInDateOptional = Optional.ofNullable(reservation.getCheckInDate());
		if (checkInDateOptional.isPresent()) {
			ZonedDateTime checkInDate = checkInDateOptional.get().toLocalDateTime().atZone(zoneId);
			String checkInDateStr = checkInDate.format(formatter);
			returnValue.setCheckInDateStr(checkInDateStr);
		}

		Optional<Timestamp> checkOutDateOptional = Optional.ofNullable(reservation.getCheckOutDate());
		if (checkOutDateOptional.isPresent()) {
			ZonedDateTime checkOutDate = checkOutDateOptional.get().toLocalDateTime().atZone(zoneId);
			String checkOutDateStr = checkOutDate.format(formatter);
			returnValue.setCheckOutDateStr(checkOutDateStr);
		}

		Optional<Timestamp> createdAtOptional = Optional.ofNullable(reservation.getCreatedAt());
		if (createdAtOptional.isPresent()) {
			ZonedDateTime createdAt = createdAtOptional.get().toLocalDateTime().atZone(zoneId);
			String createdAtStr = createdAt.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		Optional<Timestamp> updatedAtOptional = Optional.ofNullable(reservation.getUpdatedAt());
		if (updatedAtOptional.isPresent()) {
			ZonedDateTime updatedAt = updatedAtOptional.get().toLocalDateTime().atZone(zoneId);
			String updatedAtStr = updatedAt.format(formatter);
			returnValue.setUpdatedAtStr(updatedAtStr);
		}

		returnValue.setPrice(Float.valueOf(decfor.format(returnValue.getPrice())));
		return returnValue;
	}

	public ReservationEntity reservationDtoToEntity(ReservationDto reservation) {
		ReservationEntity returnValue = mapper.map(reservation, ReservationEntity.class);

		Optional<Integer> roomIdOpt = Optional.ofNullable(reservation.getRoomId());
		if (roomIdOpt.isPresent()) {
			Integer roomId = roomIdOpt.get();
			roomRepository.findById(roomId).ifPresent(returnValue::setRoom);
		}

		Optional<Integer> guestIdOpt = Optional.ofNullable(reservation.getGuestId());
		if (guestIdOpt.isPresent()) {
			Integer guestId = guestIdOpt.get();
			guestRepository.findById(guestId).ifPresent(returnValue::setGuest);
		}

		returnValue.setPrice(Float.valueOf(decfor.format(returnValue.getPrice())));
		return returnValue;
	}

	public NoteDto noteEntityToDto(NoteEntity noteEntity) {
		NoteDto returnValue = mapper.map(noteEntity, NoteDto.class);
		Optional<Timestamp> createdAtOptional = Optional.ofNullable(noteEntity.getCreatedAt());
		if (createdAtOptional.isPresent()) {
			ZonedDateTime createdAt = createdAtOptional.get().toLocalDateTime().atZone(zoneId);
			String createdAtStr = createdAt.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public NoteEntity noteDtoToEntity(NoteDto noteDto) {
		return mapper.map(noteDto, NoteEntity.class);
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled((short) userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOptional = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOptional.isEmpty()) {
			rolesOptional.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		returnValue.setEnabled(userDto.getEnabled().byteValue());
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOptional = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOptional.isEmpty()) {
			rolesIdsOptional.get().forEach((roleId) -> {
				roleRepository.findById(roleId).ifPresent(roles::add);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> usersOptional = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!usersOptional.isEmpty()) {
			usersOptional.get().forEach((user) -> {
				userIds.add(user.getId());
			});
		}

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIdsOptional = Optional.ofNullable(roleDto.getUsersIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIdsOptional.isEmpty()) {
			usersIdsOptional.get().forEach((userId) -> {
				userRepository.findById(userId).ifPresent(users::add);
			});
		}

		returnValue.setUsers(users);
		return returnValue;
	}

	public Timestamp getCurrentUTCTimestamp() {
		ZonedDateTime currentTime = Instant.now().atZone(zoneId);
		return Timestamp.valueOf(currentTime.toLocalDateTime());
	}
}
