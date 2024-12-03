package acmelab.booking.controller;

import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import acmelab.booking.payload.BookingRequest;
import acmelab.booking.payload.BookingResponse;
import acmelab.booking.service.BookingService;
import acmelab.booking.validation.Iso8601;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid BookingRequest bookingRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookingService.createBooking(bookingRequest));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable("id") @Valid Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<BookingResponse> searchBookings(@RequestParam(value = "date") @Iso8601 String date,
                                                @RequestParam(value = "room") @Valid  Room room,
                                                @RequestParam(value = "status", defaultValue = "SCHEDULED",
                                                        required = false) @Valid BookingStatus status) {
        Instant searchDate = Instant.parse(date);
        return bookingService.searchBookings(room, status, searchDate);
    }

    @GetMapping("/search-range")
    public List<BookingResponse> searchBookings(@RequestParam("date-from") @Iso8601 String dateFrom,
                                                @RequestParam("date-to") @Iso8601 String dateTo,
                                                @RequestParam("room") @Valid Room room,
                                                @RequestParam(value = "status", defaultValue = "SCHEDULED",
                                                        required = false) @Valid BookingStatus status) {
        Instant searchDateFrom = Instant.parse(dateFrom);
        Instant searchDateTo = Instant.parse(dateTo);
        return bookingService.searchBookings(room, status, searchDateFrom, searchDateTo);
    }
}
