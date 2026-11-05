package com.pradeep.smart_parking_api.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pradeep.smart_parking_api.model.Booking;
@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{


	List<Booking> findByParkingSlotId(Long slotId);

	List<Booking> findByUserId(Long userId);


}
