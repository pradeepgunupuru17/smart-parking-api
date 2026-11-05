package com.pradeep.smart_parking_api.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pradeep.smart_parking_api.model.ParkingSlot;

@Service 
public interface ParkingSlotService {

	    // Create a new parking slot
	    ParkingSlot createSlot(ParkingSlot slot);

	    // Get a slot by ID
	    ParkingSlot getSlotById(Long id);

	    // Get all slots
	    List<ParkingSlot> getAllSlots();

	    // Update slot details
	    ParkingSlot updateSlot(Long id, ParkingSlot slot);

	    // Delete a slot
	    void deleteSlot(Long id);

	    // Find slot by slot number
	    ParkingSlot getSlotByNumber(String slotNumber);

	    // Get all available slots
	    List<ParkingSlot> getAvailableSlots();

	    // Change slot status (BOOKED / AVAILABLE)
	    ParkingSlot updateSlotStatus(Long id, String status);
	}


