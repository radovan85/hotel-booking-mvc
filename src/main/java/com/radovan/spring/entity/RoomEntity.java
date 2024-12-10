package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class RoomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "room_id")
	private Integer roomId;

	@Column(name = "room_number", nullable = false,unique = true)
	private Integer roomNumber;

	@Column(nullable = false)
	private Float price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private RoomCategoryEntity roomCategory;

	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<ReservationEntity> reservations;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public RoomCategoryEntity getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(RoomCategoryEntity roomCategory) {
		this.roomCategory = roomCategory;
	}

	public List<ReservationEntity> getReservations() {
		return reservations;
	}

	public void setReservations(List<ReservationEntity> reservations) {
		this.reservations = reservations;
	}

}
