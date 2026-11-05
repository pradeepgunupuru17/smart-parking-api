package com.pradeep.smart_parking_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter

public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@ManyToOne
	@JoinColumn(name = "users_id")
	Users user;
	@ManyToOne
	@JoinColumn(name = "slot_id")
	ParkingSlot parkingSlot;
	LocalDateTime bookingTime;
	String status;
}
