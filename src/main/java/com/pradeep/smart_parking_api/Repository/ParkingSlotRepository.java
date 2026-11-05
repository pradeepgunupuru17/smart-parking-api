package com.pradeep.smart_parking_api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pradeep.smart_parking_api.model.ParkingSlot;
@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot,Long>{

	ParkingSlot findBySlotNumber(String slotNumber);

	List<ParkingSlot> findByStatus(String string);

}
