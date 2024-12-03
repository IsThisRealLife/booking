package acmelab.booking.validation;

import acmelab.booking.payload.BookingRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.Instant;

public class BookingDurationValidator implements ConstraintValidator<BookingDuration, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest bookingRequest, ConstraintValidatorContext ctx) {
        Instant startedAt = bookingRequest.getStartedAt();
        Instant endedAt = bookingRequest.getEndedAt();

        if (startedAt == null || endedAt == null) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Both startedAt and endedAt must be provided").addConstraintViolation();
            return false;
        }

        Duration duration = Duration.between(startedAt, endedAt);

        if (duration.isNegative()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Booking end date must be after the start date")
                    .addConstraintViolation();
            return false;
        }

        if (duration.toHours() < 1 || duration.toMinutes() % 60 != 0) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate("Booking duration must be at least 1 hour or multiples of 1 hour (2,3,4...)")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
