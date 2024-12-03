package acmelab.booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingBase> bookings = new HashSet<>();

    public void addBooking(BookingBase booking) {
        bookings.add(booking);
        booking.setEmployee(this);
    }

    public void removeBooking(BookingBase booking) {
        bookings.remove(booking);
        booking.setEmployee(null);
    }
}
