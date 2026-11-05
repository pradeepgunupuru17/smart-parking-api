
	package com.pradeep.smart_parking_api.Controller;

	import java.util.List;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.*;

	import com.pradeep.smart_parking_api.Service.ParkingSlotService;
import com.pradeep.smart_parking_api.model.ParkingSlot;

	@RestController
	@RequestMapping("/api/slots")
	public class ParkingSlotController {

	    @Autowired
	    private ParkingSlotService slotService;

	    // Create a new slot
	    @PostMapping
	    public ParkingSlot createSlot(@RequestBody ParkingSlot slot) {
	        return slotService.createSlot(slot);
	    }

	    // Get slot by ID
	    @GetMapping("/{id}")
	    public ParkingSlot getSlotById(@PathVariable Long id) {
	        return slotService.getSlotById(id);
	    }

	    // Get all slots
	    @GetMapping
	    public List<ParkingSlot> getAllSlots() {
	        return slotService.getAllSlots();
	    }

	    // Update slot
	    @PutMapping("/{id}")
	    public ParkingSlot updateSlot(@PathVariable Long id, @RequestBody ParkingSlot slotDetails) {
	        return slotService.updateSlot(id, slotDetails);
	    }

	    // Delete slot
	    @DeleteMapping("/{id}")
	    public void deleteSlot(@PathVariable Long id) {
	        slotService.deleteSlot(id);
	    }

	    // Get slot by number
	    @GetMapping("/number/{slotNumber}")
	    public ParkingSlot getSlotByNumber(@PathVariable String slotNumber) {
	        return slotService.getSlotByNumber(slotNumber);
	    }

	    // Get available slots
	    @GetMapping("/available")
	    public List<ParkingSlot> getAvailableSlots() {
	        return slotService.getAvailableSlots();
	    }

	    // Update slot status
	    @PutMapping("/{id}/status")
	    public ParkingSlot updateSlotStatus(@PathVariable Long id, @RequestParam String status) {
	        return slotService.updateSlotStatus(id, status);
	    }
	}


