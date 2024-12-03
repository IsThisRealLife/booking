package acmelab.booking.service;

import acmelab.booking.model.ArchivedBooking;
import acmelab.booking.model.Booking;
import acmelab.booking.model.BookingBase;
import acmelab.booking.repository.ArchivedBookingRepository;
import acmelab.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingArchivalService {

    private final BookingRepository bookingRepository;
    private final ArchivedBookingRepository archivedBookingRepository;

    @Scheduled(cron = "0 0 0 ? * MON", zone = "UTC")
    public void archiveBookings() {

        List<ArchivedBooking> archivedBookings = new ArrayList<>();

        for (Booking booking : bookingRepository.findAll()) {
            ArchivedBooking archivedBooking = new ArchivedBooking();
            archivedBooking.setStartedAt(booking.getStartedAt());
            archivedBooking.setEndedAt(booking.getEndedAt());
            archivedBooking.setRoom(booking.getRoom());
            archivedBooking.setStatus(booking.getStatus());
            archivedBooking.setEmployee(booking.getEmployee());
            archivedBooking.setArchivedAt(Instant.now());
            archivedBookings.add(archivedBooking);

        }
        archivedBookingRepository.saveAll(archivedBookings);
        log.info("Archived {} bookings", archivedBookings.size());
    }
}