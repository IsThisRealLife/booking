package acmelab.booking.payload;

import acmelab.booking.validation.BookingDuration;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.Instant;

@Data
@BookingDuration
public class BookingRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String room;

    @FutureOrPresent
    private Instant startedAt;

    @FutureOrPresent
    private Instant endedAt;

}
