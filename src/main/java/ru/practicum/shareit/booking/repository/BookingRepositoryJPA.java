package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<Booking> findByBookerAndStartBeforeAndEndAfter(User booker,
                                                        LocalDateTime now, LocalDateTime now2, Pageable pageable);

    List<Booking> findByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime now);

    Page<Booking> findByBookerAndEndBefore(User booker, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime now);

    Page<Booking> findByBookerAndStartAfter(User booker, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    Page<Booking> findByBookerAndStatus(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerOrderByStartDesc(User booker);

    Page<Booking> findByBooker(User booker, Pageable pageable);

    List<Booking> findByBookerAndItemAndEndBefore(User booker, Item item, LocalDateTime now);

    @Query("select b from Booking b left join b.item AS i " +
            "where i.owner=:user")
    Page<Booking> findAllBookingByItemJpql(@Param("user") User user, Pageable pageable);

    @Query(value = "select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND (b.start < :nowDT AND b.end>:nowDT)")
    Page<Booking> findCurrentBookingByItem(User user, LocalDateTime nowDT, Pageable pageable);

    @Query(value = "select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.end < :nowDT")
    Page<Booking> findPastBookingByItem(User user, LocalDateTime nowDT, Pageable pageable);

    @Query(value = "select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.start > :nowDT")
    Page<Booking> findFutureBookingByItem(User user, LocalDateTime nowDT, Pageable pageable);

    @Query(value = "select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.status=:status")
    Page<Booking> findBookingByItemByStatus(User user, BookingStatus status, Pageable pageable);

}
