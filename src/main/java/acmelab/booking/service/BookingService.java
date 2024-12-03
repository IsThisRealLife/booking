package acmelab.booking.service;

import acmelab.booking.exception.BusinessException;
import acmelab.booking.exception.BusinessExceptionReason;
import acmelab.booking.model.*;
import acmelab.booking.payload.BookingRequest;
import acmelab.booking.payload.BookingResponse;
import acmelab.booking.payload.BookingResponseMapper;
import acmelab.booking.repository.BookingBaseRepository;
import acmelab.booking.repository.BookingRepository;
import acmelab.booking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final BookingSearchResolver bookingSearchResolver;
    private final BookingResponseMapper bookingResponseMapper;
    private final BookingBaseRepository bookingBaseRepository;

    public BookingResponse createBooking(final BookingRequest bookingRequest) {

        Employee employee = employeeRepository.findByEmail(bookingRequest.getEmail())
                .orElseThrow(() -> new BusinessException(BusinessExceptionReason.EMPLOYEE_NOT_FOUND));

        List<Booking> overlappingBookings = bookingRepository.searchOverlappingBookingByStartedAt(
                Room.fromString(bookingRequest.getRoom()),
                BookingStatus.SCHEDULED,
                bookingRequest.getStartedAt()
        );

        if(!overlappingBookings.isEmpty()) {
            throw new BusinessException(BusinessExceptionReason.OVERLAPPING_TIME_SLOT);
        }

        Booking booking = new Booking();
        booking.setStartedAt(bookingRequest.getStartedAt());
        booking.setEndedAt(bookingRequest.getEndedAt());
        booking.setRoom(Room.fromString(bookingRequest.getRoom()));
        booking.setStatus(BookingStatus.SCHEDULED);
        booking.setEmployee(employee);

        return bookingResponseMapper.apply(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> searchBookings(Room room, BookingStatus status, Instant searchDate) {
        return bookingSearchResolver
                .searchBookings(searchDate, room, status)
                .stream()
                .map(bookingResponseMapper)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> searchBookings(Room room, BookingStatus status,
                                                Instant searchDateFrom, Instant searchDateTo) {
        return bookingSearchResolver
                .searchBookings(searchDateFrom, searchDateTo, room, status)
                .stream()
                .map(bookingResponseMapper)
                .toList();
    }

    @Transactional
    public void cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessExceptionReason.BOOKING_NOT_FOUND));

        if(booking.getStatus() == BookingStatus.CANCELED) {
            throw new BusinessException(BusinessExceptionReason.BOOKING_ALREADY_CANCELED);
        }

        if(booking.getStartedAt().isBefore(Instant.now())) {
            throw new BusinessException(BusinessExceptionReason.BOOKING_CANCELLATION_NOT_ALLOWED, booking.getStartedAt());
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByEmployeeAndFilters(Long employeeId, BookingStatus status, Instant from, Instant to) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new BusinessException(BusinessExceptionReason.EMPLOYEE_NOT_FOUND);
        }

        return bookingBaseRepository.findByEmployeeIdAndStatusAndStartedAtBetween(employeeId, status, from, to)
                .stream()
                .map(bookingResponseMapper)
                .toList();
    }
}
