package com.pradeep.smart_parking_api.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pradeep.smart_parking_api.Repository.BookingRepository;
import com.pradeep.smart_parking_api.Repository.ParkingSlotRepository;
import com.pradeep.smart_parking_api.Repository.UsersRepository;
import com.pradeep.smart_parking_api.exception.ResourceNotFoundException;
import com.pradeep.smart_parking_api.exception.SlotNotAvailableException;
import com.pradeep.smart_parking_api.model.Booking;
import com.pradeep.smart_parking_api.model.ParkingSlot;
import com.pradeep.smart_parking_api.model.Users;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Users makeUser() {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        return user;
    }

    private ParkingSlot makeSlot(String status) {
        ParkingSlot slot = new ParkingSlot();
        slot.setId(4L);
        slot.setSlotNumber("B1");
        slot.setStatus(status);
        return slot;
    }

    // Test 1: free slot -> booking created, slot becomes BOOKED
    @Test
    void createBooking_whenSlotAvailable_bookingIsCreatedAndSlotBecomesBooked() {
        ParkingSlot slot = makeSlot("AVAILABLE");
        when(usersRepository.findById(1L)).thenReturn(Optional.of(makeUser()));
        when(slotRepository.findById(4L)).thenReturn(Optional.of(slot));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.createBooking(1L, 4L);

        assertEquals("BOOKED", result.getStatus());
        assertEquals("BOOKED", slot.getStatus());
    }

    // Test 2: taken slot -> error, nothing saved
    @Test
    void createBooking_whenSlotAlreadyBooked_throwsSlotNotAvailable() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(makeUser()));
        when(slotRepository.findById(4L)).thenReturn(Optional.of(makeSlot("BOOKED")));

        assertThrows(SlotNotAvailableException.class, () -> bookingService.createBooking(1L, 4L));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    // Test 3: unknown user -> not found
    @Test
    void createBooking_whenUserNotFound_throwsResourceNotFound() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(99L, 4L));
    }

    // Test 4: cancel -> booking CANCELLED, slot AVAILABLE
    @Test
    void cancelBooking_whenBooked_bookingIsCancelledAndSlotBecomesAvailable() {
        ParkingSlot slot = makeSlot("BOOKED");
        Booking booking = new Booking();
        booking.setStatus("BOOKED");
        booking.setParkingSlot(slot);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.cancelBooking(1L);

        assertEquals("CANCELLED", result.getStatus());
        assertEquals("AVAILABLE", slot.getStatus());
    }

    // Test 5: cancel twice -> error
    @Test
    void cancelBooking_whenAlreadyCancelled_throwsSlotNotAvailable() {
        Booking booking = new Booking();
        booking.setStatus("CANCELLED");
        booking.setParkingSlot(makeSlot("AVAILABLE"));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(SlotNotAvailableException.class, () -> bookingService.cancelBooking(1L));
    }
}