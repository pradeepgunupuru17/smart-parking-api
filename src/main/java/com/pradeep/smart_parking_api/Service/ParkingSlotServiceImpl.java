package com.pradeep.smart_parking_api.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pradeep.smart_parking_api.Repository.BookingRepository;
import com.pradeep.smart_parking_api.Repository.ParkingSlotRepository;
import com.pradeep.smart_parking_api.exception.ResourceNotFoundException;
import com.pradeep.smart_parking_api.exception.SlotNotAvailableException;
import com.pradeep.smart_parking_api.model.Booking;
import com.pradeep.smart_parking_api.model.ParkingSlot;

@Service
public class ParkingSlotServiceImpl implements ParkingSlotService {

	@Autowired
	private ParkingSlotRepository slotRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Override
	public ParkingSlot createSlot(ParkingSlot slot) {
		return slotRepository.save(slot);
	}

	@Override
	public ParkingSlot getSlotById(Long id) {
		return slotRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + id));
	}

	@Override
	public List<ParkingSlot> getAllSlots() {
		return slotRepository.findAll();
	}

	@Override
	public ParkingSlot updateSlot(Long id, ParkingSlot slotDetails) {
		ParkingSlot slot = slotRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + id));

		slot.setSlotNumber(slotDetails.getSlotNumber());
		slot.setStatus(slotDetails.getStatus());
		slot.setLocation(slotDetails.getLocation());

		return slotRepository.save(slot);
	}

	@Override
	@Transactional
	public void deleteSlot(Long id) {
		ParkingSlot slot = slotRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + id));

		List<Booking> bookings = bookingRepository.findByParkingSlotId(id);

		boolean hasActiveBooking = bookings.stream().anyMatch(b -> "BOOKED".equals(b.getStatus()));
		if (hasActiveBooking) {
			throw new SlotNotAvailableException("Slot has an active booking. Cancel the booking first.");
		}

		// only old cancelled bookings are left, remove them so the slot can be deleted
		bookingRepository.deleteAll(bookings);
		slotRepository.delete(slot);
	}

	@Override
	public ParkingSlot getSlotByNumber(String slotNumber) {
		return slotRepository.findBySlotNumber(slotNumber);
	}

	@Override
	public List<ParkingSlot> getAvailableSlots() {
		return slotRepository.findByStatus("AVAILABLE");
	}

	@Override
	public ParkingSlot updateSlotStatus(Long id, String status) {
		ParkingSlot slot = slotRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + id));
		slot.setStatus(status);
		return slotRepository.save(slot);
	}
}