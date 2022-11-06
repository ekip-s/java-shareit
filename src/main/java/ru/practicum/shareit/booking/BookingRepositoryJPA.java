package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepositoryJPA extends JpaRepository<Booking, Long> {
    List<Booking> findByItemIdAndEndAfterAndStartBefore(Item item, Date start, Date end);
    List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, Date now, Date now2); //CURRENT
    List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, Date now); //"PAST"
    List<Booking> findByBookerAndStartAfterOrderByStartDesc(User booker, Date now); //FUTURE
    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);
    List<Booking> findByBookerOrderByStartDesc(User booker);
}
