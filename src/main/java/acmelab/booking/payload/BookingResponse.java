package acmelab.booking.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;
    private String email;
    private String room;
    private Instant startedAt;
    private Instant endedAt;
    private String status;

}
