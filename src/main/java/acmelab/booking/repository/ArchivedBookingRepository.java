package acmelab.booking.repository;

import acmelab.booking.model.ArchivedBooking;
import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ArchivedBookingRepository extends JpaRepository<ArchivedBooking, Long> {

    @Query("SELECT b FROM ArchivedBooking b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND DATE(b.startedAt) = DATE(:searchDate)")
    List<ArchivedBooking> searchBookingsByStartedAt(Room room, BookingStatus status, Instant searchDate);

    @Query("SELECT b FROM ArchivedBooking b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND DATE(b.startedAt) BETWEEN DATE(:createdAtFrom) AND DATE(:createdAtTo)")
    List<ArchivedBooking> searchBookingsByStartedAtBetween(Room room, BookingStatus status, Instant createdAtFrom,
                                                           Instant createdAtTo);


}