package acmelab.booking.repository;

import acmelab.booking.model.Booking;
import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Benchmark 1_000_000 bookings
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    //fast ~400ms but n + 1 problem
    @Query("SELECT b FROM Booking b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND DATE(b.startedAt) = DATE(:createdAt)")
    List<Booking> searchBookingsByStartedAt(Room room, BookingStatus status, Instant createdAt);


//
//    //slow ~2.5sec
//    @Query("SELECT b FROM Booking b " +
//            "JOIN FETCH b.employee " +
//            "WHERE b.room = :room AND DATE(b.startedAt) = DATE(:createdAt)")
//    List<Booking> searchBookingsByStartedAt(Instant createdAt, Room room);

    //slow ~2.5sec
//    @Query("SELECT new acmelab.booking.repository.dto.BookingDto(b.id, e.email, b.room, b.startedAt, b.endedAt, b.status) " +
//            "FROM Booking b " +
//            "JOIN b.employee e " +
//            "WHERE b.room = :room AND DATE(b.startedAt) = DATE(:createdAt)")
//    List<BookingDto> searchBookingsByStartedAt(Instant createdAt, Room room);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND b.startedAt <= :startedAt AND b.endedAt >= :startedAt")
    List<Booking> searchOverlappingBookingByStartedAt(Room room, BookingStatus status,
                                                   Instant startedAt);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND DATE(b.startedAt) BETWEEN DATE(:createdAtFrom) AND DATE(:createdAtTo)")
    List<Booking> searchBookingsByStartedAtBetween(Room room, BookingStatus status,
                                                   Instant createdAtFrom, Instant createdAtTo);


}
