package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;
import javax.validation.constraints.Future;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Future(message = "Дата должна быть в будущем.")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_dt")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date start;

    @Future(message = "Дата должна быть в будущем.")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_dt")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item itemId;

    @ManyToOne
    @JoinColumn(name = "booker", referencedColumnName = "id")
    private User booker;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus status;

    public Booking(Date start, Date end, Item itemId, User booker) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.booker = booker;
        this.status = BookingStatus.WAITING;
    }

    public void truncateResponse() {
        itemId.setOwner(null);
        booker.setEmail("");
        booker.setName("");
    }


}
