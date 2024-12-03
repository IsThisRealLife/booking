package acmelab.booking.repository;

import acmelab.booking.model.BookingBase;
import acmelab.booking.model.BookingStatus;
import acmelab.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingBaseRepository extends JpaRepository<BookingBase, Long>  {

    @Query("SELECT b FROM BookingBase b " +
            "WHERE b.room = :room AND b.status = :status " +
            "AND DATE(b.startedAt) BETWEEN DATE(:createdAtFrom) AND DATE(:createdAtTo)")
    List<BookingBase> searchAllBookingsByStartedAtBetween(Room room, BookingStatus status,
                                                        Instant createdAtFrom, Instant createdAtTo);

    @Query ("SELECT b FROM BookingBase b " +
            "WHERE b.employee.id = :employeeId AND b.status = :status " +
            "AND b.startedAt BETWEEN DATE(:createdAtFrom) AND DATE(:createdAtTo)")
    List<BookingBase> findByEmployeeIdAndStatusAndStartedAtBetween(Long employeeId, BookingStatus status,
                                                               Instant createdAtFrom, Instant createdAtTo);
}
