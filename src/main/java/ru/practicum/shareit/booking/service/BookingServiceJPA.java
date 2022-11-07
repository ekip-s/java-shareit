package ru.practicum.shareit.booking.service;


import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.controller.RequestParameters;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepositoryJPA;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class BookingServiceJPA implements BookingService {

    BookingRepositoryJPA bookingRepositoryJPA;
    ItemRepositoryJPA itemRepositoryJPA;
    UserServiceJPA userServiceJPA;
    EntityManager entityManager;

    @Autowired
    public BookingServiceJPA(BookingRepositoryJPA bookingRepositoryJPA,
                             ItemRepositoryJPA itemRepositoryJPA,
                             UserServiceJPA userServiceJPA,
                             EntityManager entityManager) {
        this.bookingRepositoryJPA = bookingRepositoryJPA;
        this.itemRepositoryJPA = itemRepositoryJPA;
        this.userServiceJPA = userServiceJPA;
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public Booking addBooking(Long userId, BookingDto bookingDto) {
        Optional<Item> optionalItem = itemRepositoryJPA.findById(bookingDto.getItemId());
        checkEntity(userId, optionalItem, bookingDto);
        checkDate(optionalItem.get(), bookingDto);
        User booker = userServiceJPA.getById(userId);
        Booking booking = new Booking(bookingDto.getStart(), bookingDto.getEnd(), optionalItem.get(),
                booker);
        bookingRepositoryJPA.save(booking);
        return booking;
    }

    @Transactional
    @Override
    public Booking setStatus(long userId, long bookingId, boolean approved) {
        Booking booking = getById(bookingId);
        checkUserAndStatus(userId, booking.getItem().getOwner().getId(), booking);
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            Item item = booking.getItem();
            itemRepositoryJPA.save(item);
        }
        return bookingRepositoryJPA.save(booking);
    }

    @Override
    public Booking getById(long bookingId) {
        return checkBooking(bookingRepositoryJPA.findById(bookingId));
    }

    @Override
    public Booking getById(long bookingId, long userId) {
        Booking booking = getById(bookingId);
        checkUserId(booking.getBooker().getId(), userId, booking.getItem().getOwner().getId());
        if (booking.getItem().getOwner().getId() != userId) {
            booking.truncateResponse();
        }
        return booking;
    }

    @Override
    public List<Booking> getBookings(long userId, RequestParameters requestParameters) {
        User user = userServiceJPA.getById(userId);
        LocalDateTime nowDT = LocalDateTime. now();
        List<Booking> bookings = new ArrayList<>();
        switch (requestParameters) {
            case ALL:
                bookings = bookingRepositoryJPA.findByBookerOrderByStartDesc(user);
                break;
            case CURRENT: //текущие
                bookings = bookingRepositoryJPA.findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                        nowDT, nowDT);
                break;
            case PAST: //завершенные
                bookings = bookingRepositoryJPA.findByBookerAndEndBeforeOrderByStartDesc(user, nowDT);
                break;
            case FUTURE: //будущие
                bookings = bookingRepositoryJPA.findByBookerAndStartAfterOrderByStartDesc(user, nowDT);
                break;
            case WAITING: //ожидающие
                bookings = bookingRepositoryJPA.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
                break;
            case REJECTED: //отмененные
                bookings = bookingRepositoryJPA.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
                break;
        }
        return truncateResponse(bookings);
    }

    @Override
    public List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters) {
        User user = userServiceJPA.getById(userId);
        LocalDateTime nowDT = LocalDateTime. now();
        List<Booking> bookings = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Query query;

        switch (requestParameters) {
            case ALL:
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user ORDER BY b.start DESC");
                query.setParameter("user", user);
                bookings = query.list();
                break;
            case CURRENT: //текущие
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user AND (b.start < :nowDT AND b.end > :nowDT) ORDER BY b.start DESC");
                query.setParameter("user", user);
                query.setParameter("nowDT", nowDT);
                bookings = query.list();
                break;
            case PAST: //завершенны
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user AND b.end < :nowDT ORDER BY b.start DESC");
                query.setParameter("user", user);
                query.setParameter("nowDT", nowDT);
                bookings = query.list();
                break;
            case FUTURE: //будущие
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user AND b.start > :nowDT ORDER BY b.start DESC");
                query.setParameter("user", user);
                query.setParameter("nowDT", nowDT);
                bookings = query.list();
                break;
            case WAITING: //ожидающие
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user AND b.status=:status ORDER BY b.start DESC");
                query.setParameter("user", user);
                query.setParameter("status", BookingStatus.WAITING);
                bookings = query.list();
                break;
            case REJECTED: //отмененные
                query = session.createQuery("select b from Booking b left join fetch b.item AS i " +
                        "where i.owner=:user AND b.status=:status ORDER BY b.start DESC");
                query.setParameter("user", user);
                query.setParameter("status", BookingStatus.REJECTED);
                bookings = query.list();
                break;
        }
        return bookings;
    }

    @Override
    public Optional<Booking> lastBooking(Item item) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select b from Booking AS b where b.item=:item" +
                " AND b.end <:now ORDER BY b.start DESC");
        query.setParameter("item", item);
        query.setParameter("now", LocalDateTime.now());
        List<Booking> bookings = query.list();
        return bookings.stream().findAny();
    }

    @Override
    public Optional<Booking> nextBooking(Item item) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select b from Booking AS b where b.item=:item" +
                " AND b.start >:now ORDER BY b.start DESC");
        query.setParameter("item", item);
        query.setParameter("now", LocalDateTime.now());
        List<Booking> bookings = query.list();
        return bookings.stream().findAny();
    }

    private List<Booking> truncateResponse(List<Booking> bookings) {
        for (Booking b: bookings) {
            b.truncateResponse();
        }
        return bookings;
    }

    private void checkEntity(long userId, Optional<Item> optionalItem, BookingDto booking) {
        if (optionalItem.isEmpty()) {
            throw new IllegalArgumentException("Такого товара нет");
        } else if (userId == optionalItem.get().getOwner().getId()) {
            throw new IllegalArgumentException("Нельзя забронировать свой товар.");
        } else if (!optionalItem.get().getAvailable()) {
            throw new ConflictException("Товар сейчас не доступер, выберите другой");
        } else if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ConflictException("Дата окончания бронирования, должна быть раньше старта блонирования.");
        }
    }


    private Booking checkBooking(Optional<Booking> booking) {
        if (booking.isEmpty()) {
            throw new IllegalArgumentException("Такого бронирования нет.");
        }
        return booking.get();
    }

    private void checkUserAndStatus(long userId, long ownerId, Booking booking) {
        if (userId != ownerId) {
            throw new IllegalArgumentException("Обновить статус может только владелец товара.");
        } else if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ConflictException("Статус обновлен ранее, текущий статус: " + booking.getStatus());
        }
    }

    private void checkDate(Item item, BookingDto bookingDto) {
            List<Booking> bookings = bookingRepositoryJPA.findByItemAndEndAfterAndStartBefore(item,
                    bookingDto.getStart(), bookingDto.getEnd());
            if (!bookings.isEmpty()) {
                throw new ConflictException("Товар недоступен в этот временной интервал.");
            }
    }

    private void checkUserId(long bookerId, long userID, long ownerId) {
        if (bookerId != userID && ownerId != userID) {
            throw new IllegalArgumentException("Бронь на другом пользователем.");
        }
    }
}
