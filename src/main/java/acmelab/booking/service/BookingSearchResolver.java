package acmelab.booking.service;

import acmelab.booking.model.BookingBase;
import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import acmelab.booking.repository.ArchivedBookingRepository;
import acmelab.booking.repository.BookingBaseRepository;
import acmelab.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static acmelab.booking.utils.DateUtils.isAfterStartOfThisWeek;
import static acmelab.booking.utils.DateUtils.isOlderThanStartOfThisWeek;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingSearchResolver {

    private final BookingBaseRepository bookingBaseRepository;
    private final BookingRepository bookingRepository;
    private final ArchivedBookingRepository archivedBookingRepository;

    public List<? extends BookingBase> searchBookings(Instant searchDate, Room room, BookingStatus status) {
        if (isAfterStartOfThisWeek(searchDate)) {
            return bookingRepository.searchBookingsByStartedAt(room, status, searchDate);
        } else {
            return archivedBookingRepository.searchBookingsByStartedAt(room, status, searchDate);
        }
    }

    public List<? extends BookingBase> searchBookings(Instant searchDateFrom, Instant searchDateTo, Room room, BookingStatus status) {
        if(isOlderThanStartOfThisWeek(searchDateFrom) && isAfterStartOfThisWeek(searchDateTo)) {
            return bookingBaseRepository.searchAllBookingsByStartedAtBetween(room, status, searchDateFrom, searchDateTo);
        } else if (isAfterStartOfThisWeek(searchDateFrom)) {
            return bookingRepository.searchBookingsByStartedAtBetween(room, status, searchDateFrom, searchDateTo);
        } else {
            return archivedBookingRepository.searchBookingsByStartedAtBetween(room, status, searchDateFrom, searchDateTo);
        }
    }

}
