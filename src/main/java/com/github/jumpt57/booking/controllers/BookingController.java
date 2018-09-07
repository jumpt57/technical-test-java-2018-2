package com.github.jumpt57.booking.controllers;

import com.github.jumpt57.booking.dto.BookingWithPassword;
import com.github.jumpt57.booking.dto.SimpleBooking;
import com.github.jumpt57.booking.services.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<Collection<SimpleBooking>> getBookings() {
        return ResponseEntity.ok(bookingService.getBookings());
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<Collection<SimpleBooking>> getBookingsForRoom(@PathVariable("roomNumber") String roomNumber) {
        return ResponseEntity.ok(bookingService.getBookingsForRoom(roomNumber));
    }

    @GetMapping("/available")
    public ResponseEntity<Collection<SimpleBooking>> getAvailableBookings() {
        return ResponseEntity.ok(bookingService.getAvailableBookings());
    }

    @PostMapping
    public ResponseEntity<Boolean> createBooking(@Valid @RequestBody BookingWithPassword bookingWithPassword) {
        return ResponseEntity.ok(bookingService.createBooking(bookingWithPassword));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteBooking(@Valid @RequestBody BookingWithPassword bookingWithPassword) {
        return ResponseEntity.ok(bookingService.deleteBooking(bookingWithPassword));
    }

}
