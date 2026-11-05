package com.pradeep.smart_parking_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.pradeep.smart_parking_api.Service.BookingService;
import com.pradeep.smart_parking_api.Service.UsersService;
import com.pradeep.smart_parking_api.model.Booking;
import com.pradeep.smart_parking_api.model.Users;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UsersService usersService;

    // Book a slot for the logged-in user (userId comes from the token)
    @PostMapping("/create")
    public Booking createBooking(@RequestParam Long slotId, Authentication auth) {
        Users user = usersService.getUserByEmail(auth.getName());
        return bookingService.createBooking(user.getId(), slotId);
    }

    // Cancel: only the owner or an admin
    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id, Authentication auth) {
        checkOwnerOrAdmin(bookingService.getBookingById(id), auth);
        return bookingService.cancelBooking(id);
    }

    // View one booking: only the owner or an admin
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id, Authentication auth) {
        Booking booking = bookingService.getBookingById(id);
        checkOwnerOrAdmin(booking, auth);
        return booking;
    }

    // Logged-in user's own bookings
    @GetMapping("/my")
    public List<Booking> getMyBookings(Authentication auth) {
        Users user = usersService.getUserByEmail(auth.getName());
        return bookingService.getBookingsByUserId(user.getId());
    }

    // ---- Admin only (rules are in SecurityConfig) ----
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping("/slot/{slotId}")
    public List<Booking> getBookingsBySlotId(@PathVariable Long slotId) {
        return bookingService.getBookingsBySlotId(slotId);
    }

    private void checkOwnerOrAdmin(Booking booking, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = booking.getUser().getEmail().equals(auth.getName());
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You can only access your own bookings");
        }
    }
}