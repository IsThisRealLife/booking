package acmelab.booking.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessExceptionReason {

    EMPLOYEE_NOT_FOUND("Employee not found", HttpStatus.NOT_FOUND),
    BOOKING_NOT_FOUND("Booking not found", HttpStatus.NOT_FOUND),
    OVERLAPPING_TIME_SLOT("Booking time slot overlaps with another booking", HttpStatus.BAD_REQUEST),
    BOOKING_ALREADY_CANCELED("Booking is already cancelled", HttpStatus.BAD_REQUEST),
    BOOKING_CANCELLATION_NOT_ALLOWED("Booking cancellation is not allowed. Started at %s", HttpStatus.BAD_REQUEST),
    ;

    private final String code = BusinessExceptionReason.class.getSimpleName();
    private final String message;
    private final HttpStatus httpStatus;

}
