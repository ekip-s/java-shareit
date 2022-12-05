package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;


@AllArgsConstructor
@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Future(message = "Дата должна быть в будущем.")
    @Column(name = "start_dt")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime start;

    @Future(message = "Дата должна быть в будущем.")
    @Column(name = "end_dt")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker", referencedColumnName = "id")
    private User booker;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus status;

    public Booking(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = BookingStatus.WAITING;
    }

    public Booking() {
    }

    public Booking truncateResponse() {
        item.setOwner(null);
        booker.setEmail("");
        booker.setName("");
        return this;
    }
}
