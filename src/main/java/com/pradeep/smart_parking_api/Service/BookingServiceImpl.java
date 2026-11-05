package com.pradeep.smart_parking_api.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pradeep.smart_parking_api.Repository.BookingRepository;
import com.pradeep.smart_parking_api.Repository.ParkingSlotRepository;
import com.pradeep.smart_parking_api.Repository.UsersRepository;
import com.pradeep.smart_parking_api.exception.ResourceNotFoundException;
import com.pradeep.smart_parking_api.exception.SlotNotAvailableException;
import com.pradeep.smart_parking_api.model.Booking;
import com.pradeep.smart_parking_api.model.ParkingSlot;
import com.pradeep.smart_parking_api.model.Users;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ParkingSlotRepository slotRepository;

	@Override
	@Transactional
	public Booking createBooking(Long userId, Long slotId) {
		Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
		ParkingSlot slot = slotRepository.findById(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + slotId));

		if (!"AVAILABLE".equals(slot.getStatus())) {
			throw new SlotNotAvailableException("Slot is not available");
		}

		Booking booking = new Booking();
		booking.setUser(user);
		booking.setParkingSlot(slot);
		booking.setBookingTime(LocalDateTime.now());
		booking.setStatus("BOOKED");

		slot.setStatus("BOOKED");
		slotRepository.save(slot);

		return bookingRepository.save(booking);
	}

	@Override
	@Transactional
	public Booking cancelBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

		if ("CANCELLED".equals(booking.getStatus())) {
			throw new SlotNotAvailableException("Booking is already cancelled");
		}

		booking.setStatus("CANCELLED");

		ParkingSlot slot = booking.getParkingSlot();
		slot.setStatus("AVAILABLE");
		slotRepository.save(slot);

		return bookingRepository.save(booking);
	}

	@Override
	public Booking getBookingById(Long id) {
		return bookingRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
	}

	@Override
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	@Override
	public List<Booking> getBookingsByUserId(Long userId) {
		return bookingRepository.findByUserId(userId);
	}

	@Override
	public List<Booking> getBookingsBySlotId(Long slotId) {
		return bookingRepository.findByParkingSlotId(slotId);
	}
}