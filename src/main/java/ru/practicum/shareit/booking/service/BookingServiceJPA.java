package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.model.RequestParameters;
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
import java.util.stream.Collectors;


@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class BookingServiceJPA implements BookingService {

    private final BookingRepositoryJPA bookingRepositoryJPA;
    private final ItemRepositoryJPA itemRepositoryJPA;
    private final UserServiceJPA userServiceJPA;
    private final EntityManager entityManager;

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
        Booking booking = new Booking(bookingDto.getStart(), bookingDto.getEnd(), optionalItem.get(), booker);
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
        List<Booking> bookings = new ArrayList<>();

        switch (requestParameters) {
            case ALL:
                bookings = bookingRepositoryJPA.findByBookerOrderByStartDesc(user);
                break;
            case CURRENT:
                bookings = bookingRepositoryJPA.findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                        LocalDateTime. now(), LocalDateTime. now());
                break;
            case PAST:
                bookings = bookingRepositoryJPA.findByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime. now());
                break;
            case FUTURE:
                bookings = bookingRepositoryJPA.findByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime. now());
                break;
            case WAITING:
                bookings = bookingRepositoryJPA.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepositoryJPA.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
                break;
        }
        return truncateResponse(bookings);
    }

    @Override
    public List<Booking> getBookings(long userId, RequestParameters requestParameters, int from, int size) {
        User user = userServiceJPA.getById(userId);
        from = checkPaginationParamsAndReturnFrom(from, size);
        Page<Booking> bookings = null;
        switch (requestParameters) {
            case ALL:
                bookings = bookingRepositoryJPA.findByBooker(user, getPage(from, size));
                break;
            case CURRENT:
                bookings = bookingRepositoryJPA.findByBookerAndStartBeforeAndEndAfter(user,
                        LocalDateTime. now(), LocalDateTime. now(), getPage(from, size));
                break;
            case PAST:
                bookings = bookingRepositoryJPA.findByBookerAndEndBefore(user,
                        LocalDateTime. now(), getPage(from, size));
                break;
            case FUTURE:
                bookings = bookingRepositoryJPA.findByBookerAndStartAfter(user,
                        LocalDateTime. now(), getPage(from, size));
                break;
            case WAITING:
                bookings = bookingRepositoryJPA.findByBookerAndStatus(user, BookingStatus.WAITING, getPage(from, size));
                break;
            case REJECTED:
                bookings = bookingRepositoryJPA.findByBookerAndStatus(user, BookingStatus.REJECTED,
                        getPage(from, size));
                break;
        }
        return truncateResponsePage(bookings);
    }

    @Override
    public List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters) {
        User user = userServiceJPA.getById(userId);
        switch (requestParameters) {
            case CURRENT:
                return bookingRepositoryJPA.getCurrentBookingByOwner(user);
            case PAST:
                return bookingRepositoryJPA.getPastBookingByOwner(user);
            case FUTURE:
                return bookingRepositoryJPA.getFutureBookingByOwner(user);
            case WAITING:
                return bookingRepositoryJPA.getBookingByOwnerAndStart(user, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepositoryJPA.getBookingByOwnerAndStart(user, BookingStatus.REJECTED);
            default:
                return bookingRepositoryJPA.getAllBookingByOwner(user);
        }
    }

    @Override
    public List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters, int from, int size) {
        User user = userServiceJPA.getById(userId);
        from = checkPaginationParamsAndReturnFrom(from, size);
        Page<Booking> bookings = null;
        switch (requestParameters) {
            case ALL:
                bookings = bookingRepositoryJPA.findAllBookingByItemJpql(user, getPage(from, size));
                break;
            case CURRENT:
                bookings = bookingRepositoryJPA.findCurrentBookingByItem(user, getPage(from, size));
                break;
            case PAST:
                System.out.println("Здесь: " + user);
                bookings = bookingRepositoryJPA.findPastBookingByItem(user, getPage(from, size));
                break;
            case FUTURE:
                bookings = bookingRepositoryJPA.findFutureBookingByItem(user, getPage(from, size));
                break;
            case WAITING:
                bookings = bookingRepositoryJPA.findBookingByItemByStatus(user, BookingStatus.WAITING,
                        getPage(from, size));
                break;
            case REJECTED:
                bookings = bookingRepositoryJPA.findBookingByItemByStatus(user, BookingStatus.REJECTED,
                        getPage(from, size));
                break;
        }
        return truncateResponsePage(bookings);
    }

    @Override
    public Optional<Booking> lastBooking(Item item) {
        return bookingRepositoryJPA.getLastBooking(item).stream().findAny();
    }

    @Override
    public Optional<Booking> nextBooking(Item item) {
        return bookingRepositoryJPA.getNextBooking(item).stream().findAny();
    }

    private List<Booking> truncateResponse(List<Booking> bookings) {
        return bookings.stream()
                .map(i -> i.truncateResponse())
                .collect(Collectors.toList());
    }

    private List<Booking> truncateResponsePage(Page<Booking> bookings) {
        return bookings.stream()
                    .map(i -> i.truncateResponse())
                    .collect(Collectors.toList());
    }



    private void checkEntity(long userId, Optional<Item> optionalItem, BookingDto booking) {
        if (optionalItem.isEmpty()) {
            throw new IllegalArgumentException("Такого товара нет");
        } else if (userId == optionalItem.get().getOwner().getId()) {
            throw new IllegalArgumentException("Нельзя забронировать свой товар.");
        } else if (!optionalItem.get().getAvailable()) {
            throw new ConflictException("Товар сейчас не доступен, выберите другой");
        } else if (booking.getStart().isAfter(booking.getEnd())) {
            throw new ConflictException("Дата окончания бронирования, должна быть раньше старта блонирования.");
        }
    }

    private int checkPaginationParamsAndReturnFrom(int from, int size) {
        if (from < 0) {
            throw new ConflictException("Параметр from не может быть отрицательным.");
        } else if (size < 1) {
            throw new ConflictException("Параметр size должен быть меньше нуля.");
        }
        return from / size;
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

    private Pageable getPage(int from, int size) {
        return PageRequest.of(from, size, Sort.by("start").descending());
    }
}
