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

    @Query("select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.start < current_timestamp AND b.end > current_timestamp")
    Page<Booking> findCurrentBookingByItem(@Param("user") User user, Pageable pageable);

    @Query("select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.end < current_timestamp")
    Page<Booking> findPastBookingByItem(@Param("user") User user, Pageable pageable);

    @Query("select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.start > current_timestamp")
    Page<Booking> findFutureBookingByItem(@Param("user") User user, Pageable pageable);

    @Query("select b from Booking b left join b.item AS i " +
            "where i.owner=:user AND b.status=:status")
    Page<Booking> findBookingByItemByStatus(@Param("user") User user, BookingStatus status, Pageable pageable);

    @Query("select b from Booking b left join fetch b.item AS i " +
            "where i.owner=:user ORDER BY b.start DESC")
    List<Booking> getAllBookingByOwner(@Param("user") User user);

    @Query("select b from Booking b left join fetch b.item AS i " +
            "where i.owner=:user AND (b.start < current_timestamp AND b.end > current_timestamp)" +
            " ORDER BY b.start DESC")
    List<Booking> getCurrentBookingByOwner(@Param("user") User user);

    @Query("select b from Booking b left join fetch b.item AS i " +
            "where i.owner=:user AND b.end < current_timestamp ORDER BY b.start DESC")
    List<Booking> getPastBookingByOwner(@Param("user") User user);

    @Query("select b from Booking b left join fetch b.item AS i " +
            "where i.owner=:user AND b.start > current_timestamp ORDER BY b.start DESC")
    List<Booking> getFutureBookingByOwner(@Param("user") User user);

    @Query("select b from Booking b left join fetch b.item AS i " +
            "where i.owner=:user AND b.status=:status ORDER BY b.start DESC")
    List<Booking> getBookingByOwnerAndStart(@Param("user") User user, @Param("status") BookingStatus status);

    @Query("select b from Booking AS b where b.item=:item" +
            " AND b.end < current_timestamp ORDER BY b.start DESC")
    List<Booking> getLastBooking(@Param("item") Item item);

    @Query("select b from Booking AS b where b.item=:item" +
            " AND b.start > current_timestamp ORDER BY b.start DESC")
    List<Booking> getNextBooking(@Param("item") Item item);
}
