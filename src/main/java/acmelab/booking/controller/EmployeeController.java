package acmelab.booking.controller;

import acmelab.booking.model.BookingStatus;
import acmelab.booking.payload.BookingResponse;
import acmelab.booking.service.BookingService;
import acmelab.booking.validation.Iso8601;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Validated
@RequiredArgsConstructor
public class EmployeeController {

    private final BookingService bookingService;

    @GetMapping("/{employeeId}/bookings")
    public ResponseEntity<List<BookingResponse>> getEmployeeBookings(
            @PathVariable Long employeeId,
            @RequestParam(value = "status", defaultValue = "SCHEDULED",
                    required = false) BookingStatus status,
            @RequestParam(value = "date-from") @Iso8601 String dateFrom,
            @RequestParam(value = "date-to") @Iso8601 String dateTo) {

        Instant from = Instant.parse(dateFrom);
        Instant to = Instant.parse(dateTo);
        List<BookingResponse> bookings = bookingService.getBookingsByEmployeeAndFilters(employeeId, status, from, to);
        return ResponseEntity.ok(bookings);
    }
}
