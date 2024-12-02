package acmelab.booking.repository;

import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;
    private String email;
    private Room room;
    private Instant startedAt;
    private Instant endedAt;
    private BookingStatus status;
}
