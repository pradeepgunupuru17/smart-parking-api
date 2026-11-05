package com.pradeep.smart_parking_api.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pradeep.smart_parking_api.model.Booking;

@Service
public interface BookingService {

	    // Create a new booking
	    Booking createBooking(Long userId, Long slotId);

	    // Cancel an existing booking
	    Booking cancelBooking(Long bookingId);

	    // Get booking by ID
	    Booking getBookingById(Long id);

	    // Get all bookings
	    List<Booking> getAllBookings();

	    // Get all bookings for a specific user
	    List<Booking> getBookingsByUserId(Long userId);

	    // Get all bookings for a specific parking slot
	    List<Booking> getBookingsBySlotId(Long slotId);
}
