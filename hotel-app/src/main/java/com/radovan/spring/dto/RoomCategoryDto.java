package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.List;

public class RoomCategoryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer roomCategoryId;

	private String name;

	private Float price;

	private Short wifi;

	private Short wc;

	private Short tv;

	private Short bar;

	private List<Integer> roomsIds;

	public Integer getRoomCategoryId() {
		return roomCategoryId;
	}

	public void setRoomCategoryId(Integer roomCategoryId) {
		this.roomCategoryId = roomCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Short getWifi() {
		return wifi;
	}

	public void setWifi(Short wifi) {
		this.wifi = wifi;
	}

	public Short getWc() {
		return wc;
	}

	public void setWc(Short wc) {
		this.wc = wc;
	}

	public Short getTv() {
		return tv;
	}

	public void setTv(Short tv) {
		this.tv = tv;
	}

	public Short getBar() {
		return bar;
	}

	public void setBar(Short bar) {
		this.bar = bar;
	}

	public List<Integer> getRoomsIds() {
		return roomsIds;
	}

	public void setRoomsIds(List<Integer> roomsIds) {
		this.roomsIds = roomsIds;
	}

}
