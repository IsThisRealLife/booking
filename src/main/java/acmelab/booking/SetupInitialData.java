package acmelab.booking;

import acmelab.booking.model.*;
import acmelab.booking.repository.ArchivedBookingRepository;
import acmelab.booking.repository.BookingRepository;
import acmelab.booking.repository.EmployeeRepository;
import acmelab.booking.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
@RequiredArgsConstructor
public class SetupInitialData implements ApplicationRunner {

    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final ArchivedBookingRepository archivedBookingRepository;

    private final List<String> emails = List.of("a@acme.gr", "b@acme.gr", "c@acme.gr", "d@acme.gr", "e@acme.gr");
    private final int bookings = 1_000_000;
    private final int batchSize = 10_000;

    public void run(ApplicationArguments args){

        if(employeeRepository.count() > 0) {
            log.info("Initial data already exists");
            return;
        }

        List<Employee> employees = new ArrayList<>();

        emails.forEach(email -> {
            Employee employee = new Employee();
            employee.setEmail(email);
            employees.add(employee);

        });

        employeeRepository.saveAll(employees);

        Set<Booking> bookingsEntities = new HashSet<>();
        Set<ArchivedBooking> archivedBookingsEntities = new HashSet<>();

        for (int i = 0; i < bookings; i++) {

            LocalDate randomDate = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(
                    LocalDate.of(2020, 1, 1).toEpochDay(),
                    LocalDate.now().plusDays(5).toEpochDay()));

            int randomHourStart = ThreadLocalRandom.current().nextInt(0, 20);
            int randomHourEnd = randomHourStart + ThreadLocalRandom.current().nextInt(1, 3);
            int randomMinute = List.of(0, 35, 45).get(ThreadLocalRandom.current().nextInt(3));

            Employee employee = employees.get(ThreadLocalRandom.current().nextInt(0, emails.size()));
            Instant startedAt = LocalDateTime.of(randomDate, LocalTime.of(randomHourStart, randomMinute)).toInstant(ZoneOffset.UTC);
            Instant endedAt = LocalDateTime.of(randomDate, LocalTime.of(randomHourEnd, randomMinute)).toInstant(ZoneOffset.UTC);
            Room room = Room.values()[ThreadLocalRandom.current().nextInt(Room.values().length)];
            BookingStatus status = BookingStatus.values()[ThreadLocalRandom.current().nextInt(BookingStatus.values().length)];

            if (DateUtils.isOlderThanStartOfThisWeek(startedAt)) {
                ArchivedBooking archivedBooking = new ArchivedBooking();
                populateBookingBase(archivedBooking, employee, startedAt, endedAt, room, status);
                archivedBooking.setArchivedAt(endedAt);
                archivedBookingsEntities.add(archivedBooking);
            } else {
                Booking booking = new Booking();
                populateBookingBase(booking, employee, startedAt, endedAt, room, status);
                bookingsEntities.add(booking);
            }

            if(i % batchSize == 0) {
                log.info("Flushing batch of {} bookings", i);
                bookingRepository.saveAll(bookingsEntities);
                archivedBookingRepository.saveAll(archivedBookingsEntities);

                bookingRepository.flush();
                archivedBookingRepository.flush();

                bookingsEntities.clear();
                archivedBookingsEntities.clear();
            }
        }

        bookingRepository.saveAll(bookingsEntities);
        archivedBookingRepository.saveAll(archivedBookingsEntities);

        bookingRepository.flush();
        archivedBookingRepository.flush();
    }

    private void populateBookingBase(BookingBase bookingBase, Employee employee, Instant startedAt,
                                     Instant endedAt, Room room, BookingStatus status) {
        bookingBase.setEmployee(employee);
        bookingBase.setStartedAt(startedAt);
        bookingBase.setEndedAt(endedAt);
        bookingBase.setRoom(room);
        bookingBase.setStatus(status);
    }
}
