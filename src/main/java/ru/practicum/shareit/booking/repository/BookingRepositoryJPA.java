package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepositoryJPA extends JpaRepository<Booking, Long> {

    List<Booking> findByItemAndEndAfterAndStartBefore(Item item, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker,
                                                                        LocalDateTime now, LocalDateTime now2);

    List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime now);

    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    List<Booking> findByBookerOrderByStartDesc(User booker);

    List<Booking> findByBookerAndItemAndEndBefore(User booker, Item item, LocalDateTime now);
}
