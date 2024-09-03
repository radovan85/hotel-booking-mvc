package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.GuestDto;
import com.radovan.spring.dto.NoteDto;
import com.radovan.spring.dto.ReservationDto;
import com.radovan.spring.dto.RoomCategoryDto;
import com.radovan.spring.dto.RoomDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.GuestService;
import com.radovan.spring.service.NoteService;
import com.radovan.spring.service.ReservationService;
import com.radovan.spring.service.RoomCategoryService;
import com.radovan.spring.service.RoomService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private GuestService guestService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoomCategoryService roomCategoryService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private NoteService noteService;

	@Autowired
	private ReservationService reservationService;

	@GetMapping(value = "/allGuests")
	public String allGuests(ModelMap map) {
		List<UserDto> allUsers = userService.listAllUsers();
		List<GuestDto> allGuests = guestService.listAll();
		map.put("allUsers", allUsers);
		map.put("allGuests", allGuests);
		map.put("recordsPerPage", 6);
		return "fragments/guestList :: fragmentContent";
	}

	@GetMapping(value = "/guestDetails/{guestId}")
	public String getGuestDetails(@PathVariable("guestId") Integer guestId, ModelMap map) {
		GuestDto guest = guestService.getGuestById(guestId);
		UserDto user = userService.getUserById(guest.getUserId());
		map.put("user", user);
		map.put("guest", guest);
		return "fragments/guestDetails :: fragmentContent";
	}

	@GetMapping(value = "/deleteGuest/{guestId}")
	public String deleteGuest(@PathVariable("guestId") Integer guestId) {
		guestService.deleteGuest(guestId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allRoomCategories")
	public String listAllCategories(ModelMap map) {
		List<RoomCategoryDto> allCategories = roomCategoryService.listAll();
		map.put("allCategories", allCategories);
		map.put("recordsPerPage", 5);
		return "fragments/roomCategoryList :: fragmentContent";
	}

	@GetMapping(value = "/createRoomCategory")
	public String renderCategoryForm(ModelMap map) {
		RoomCategoryDto roomCategory = new RoomCategoryDto();
		map.put("roomCategory", roomCategory);
		return "fragments/roomCategoryForm :: fragmentContent";
	}

	@PostMapping(value = "/createRoomCategory")
	public String storeRoomCategory(@ModelAttribute("roomCategory") RoomCategoryDto roomCategory) {
		roomCategoryService.addCategory(roomCategory);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/categoryDetails/{categoryId}")
	public String getCategoryDetails(@PathVariable("categoryId") Integer categoryId, ModelMap map) {
		RoomCategoryDto roomCategory = roomCategoryService.getCategoryById(categoryId);
		map.put("roomCategory", roomCategory);
		return "fragments/roomCategoryDetails :: fragmentContent";
	}

	@GetMapping(value = "/updateRoomCategory/{categoryId}")
	public String renderCategoryUpdateForm(@PathVariable("categoryId") Integer categoryId, ModelMap map) {
		RoomCategoryDto roomCategory = new RoomCategoryDto();
		RoomCategoryDto currentCategory = roomCategoryService.getCategoryById(categoryId);
		map.put("roomCategory", roomCategory);
		map.put("currentCategory", currentCategory);
		return "fragments/updateRoomCategory :: fragmentContent";
	}

	@GetMapping(value = "/deleteRoomCategory/{categoryId}")
	public String deleteCategory(@PathVariable("categoryId") Integer categoryId) {
		List<RoomDto> allRooms = roomService.listAllByCategoryId(categoryId);
		for (RoomDto room : allRooms) {
			reservationService.deleteAllByRoomId(room.getRoomId());
		}

		roomService.deleteAllByCategoryId(categoryId);
		roomCategoryService.deleteCategory(categoryId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allRooms")
	public String listAllRooms(ModelMap map) {
		List<RoomDto> allRooms = roomService.listAll();
		List<RoomCategoryDto> allCategories = roomCategoryService.listAll();
		map.put("allRooms", allRooms);
		map.put("allCategories", allCategories);
		map.put("recordsPerPage", 6);
		return "fragments/roomList :: fragmentContent";
	}

	@GetMapping(value = "/createRoom")
	public String renderRoomForm(ModelMap map) {
		RoomDto room = new RoomDto();
		List<RoomCategoryDto> allCategories = roomCategoryService.listAll();
		map.put("room", room);
		map.put("allCategories", allCategories);
		return "fragments/roomForm :: fragmentContent";
	}

	@PostMapping(value = "/createRoom")
	public String storeRoom(@ModelAttribute("room") RoomDto room) {
		roomService.addRoom(room);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/roomDetails/{roomId}")
	public String getRoomDetails(@PathVariable("roomId") Integer roomId, ModelMap map) {
		RoomDto room = roomService.getRoomById(roomId);
		RoomCategoryDto category = roomCategoryService.getCategoryById(room.getRoomCategoryId());
		map.put("room", room);
		map.put("category", category);
		return "fragments/roomDetails :: fragmentContent";
	}

	@GetMapping(value = "/updateRoom/{roomId}")
	public String renderRoomUpdateForm(@PathVariable("roomId") Integer roomId, ModelMap map) {
		RoomDto room = new RoomDto();
		RoomDto currentRoom = roomService.getRoomById(roomId);
		List<RoomCategoryDto> allCategories = roomCategoryService.listAll();
		map.put("room", room);
		map.put("currentRoom", currentRoom);
		map.put("allCategories", allCategories);
		return "fragments/updateRoom :: fragmentContent";
	}

	@GetMapping(value = "/deleteRoom/{roomId}")
	public String deleteRoom(@PathVariable("roomId") Integer roomId) {
		reservationService.deleteAllByRoomId(roomId);
		roomService.deleteRoom(roomId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allNotes")
	public String listAllNotes(ModelMap map) {
		List<NoteDto> allNotes = noteService.listAll();
		map.put("allNotes", allNotes);
		map.put("recordsPerPage", 10);
		return "fragments/noteList :: fragmentContent";
	}

	@GetMapping(value = "/allNotesToday")
	public String listAllNotesForToday(ModelMap map) {
		List<NoteDto> allNotes = noteService.listAllForToday();
		map.put("allNotes", allNotes);
		map.put("recordsPerPage", 10);
		return "fragments/noteList :: fragmentContent";
	}

	@GetMapping(value = "/noteDetails/{noteId}")
	public String getNoteDetails(@PathVariable("noteId") Integer noteId, ModelMap map) {
		NoteDto note = noteService.getNoteById(noteId);
		map.put("note", note);
		return "fragments/noteDetails :: fragmentContent";
	}

	@GetMapping(value = "/deleteNote/{noteId}")
	public String deleteNote(@PathVariable("noteId") Integer noteId) {
		noteService.deleteNote(noteId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/deleteAllNotes")
	public String deleteAllNotes() {
		noteService.deleteAllNotes();
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allReservations")
	public String listAllReservations(ModelMap map) {
		List<ReservationDto> allReservations = reservationService.listAll();
		List<GuestDto> allGuests = guestService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allReservations", allReservations);
		map.put("allGuests", allGuests);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reservationList :: fragmentContent";
	}

	@GetMapping(value = "/allActiveReservations")
	public String listAllActiveReservations(ModelMap map) {
		List<ReservationDto> allReservations = reservationService.listAllActive();
		List<GuestDto> allGuests = guestService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allReservations", allReservations);
		map.put("allGuests", allGuests);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reservationList :: fragmentContent";
	}

	@GetMapping(value = "/allExpiredReservations")
	public String listAllExpiredReservations(ModelMap map) {
		List<ReservationDto> allReservations = reservationService.listAllExpired();
		List<GuestDto> allGuests = guestService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allReservations", allReservations);
		map.put("allGuests", allGuests);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 6);
		return "fragments/reservationList :: fragmentContent";
	}

	@GetMapping(value = "/reservationDetails/{reservationId}")
	public String getReservationDetails(@PathVariable("reservationId") Integer reservationId, ModelMap map) {
		ReservationDto reservation = reservationService.getReservationById(reservationId);
		RoomDto room = roomService.getRoomById(reservation.getRoomId());
		RoomCategoryDto category = roomCategoryService.getCategoryById(room.getRoomCategoryId());
		GuestDto guest = guestService.getGuestById(reservation.getGuestId());
		UserDto user = userService.getUserById(guest.getUserId());
		// String createdAtStr = reservation.getCreatedAt();
		// String updatedAtStr = reservation.getUpdatedAt();
		map.put("room", room);
		map.put("category", category);
		map.put("reservation", reservation);
		map.put("user", user);
		// map.put("createdAtStr", createdAtStr);
		// map.put("updatedAtStr", updatedAtStr);
		return "fragments/reservationDetails :: fragmentContent";
	}

	@GetMapping(value = "/switchReservationRoom/{reservationId}")
	public String changeReservationRoom(@PathVariable("reservationId") Integer reservationId, ModelMap map) {
		ReservationDto reservation = new ReservationDto();
		ReservationDto currentReservation = reservationService.getReservationById(reservationId);
		RoomDto currentRoom = roomService.getRoomById(currentReservation.getRoomId());
		List<RoomDto> availableRooms = roomService.listAllByCategoryId(currentRoom.getRoomCategoryId());
		RoomCategoryDto category = roomCategoryService.getCategoryById(currentRoom.getRoomCategoryId());
		availableRooms.removeIf(obj -> obj.getRoomId() == currentRoom.getRoomId());
		map.put("reservation", reservation);
		map.put("currentReservation", currentReservation);
		map.put("currentRoom", currentRoom);
		map.put("availableRooms", availableRooms);
		map.put("category", category);
		return "fragments/switchRoomForm :: fragmentContent";
	}

	@PostMapping(value = "/updateReservation/{reservationId}")
	public String updateReservation(@ModelAttribute ReservationDto reservation,
			@PathVariable("reservationId") Integer reservationId) {
		reservationService.updateReservation(reservation, reservationId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/deleteReservation/{reservationId}")
	public String deleteReservation(@PathVariable("reservationId") Integer reservationId) {
		reservationService.deleteReservation(reservationId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/roomNumberError")
	public String roomError() {
		return "fragments/roomNumberError :: fragmentContent";
	}

}
