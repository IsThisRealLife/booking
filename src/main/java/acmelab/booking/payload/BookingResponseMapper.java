package acmelab.booking.payload;

import acmelab.booking.model.BookingBase;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookingResponseMapper implements Function<BookingBase, BookingResponse> {

    @Override
    public BookingResponse apply(BookingBase booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getEmployee().getEmail(),
                booking.getRoom().getRoomName(),
                booking.getStartedAt(),
                booking.getEndedAt(),
                booking.getStatus().name());
    }
}
