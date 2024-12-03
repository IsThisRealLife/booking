package acmelab.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "archived_booking")
@Getter
@Setter
public class ArchivedBooking extends BookingBase {

    public Instant archivedAt;
}
