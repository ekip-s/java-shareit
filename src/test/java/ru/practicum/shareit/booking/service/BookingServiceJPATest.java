package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.RequestParameters;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceJPATest {

    private final BookingServiceJPA bookingServiceJPA;
    private final UserServiceJPA userServiceJPA;
    private final ItemServiceJPA itemServiceJPA;
    private User user;
    private User user2;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private Item item;
    private Item item2;
    private BookingDto bookingDto;
    private BookingDto bookingDto2;

    @BeforeEach
    void createTest() {
        user = userServiceJPA.saveUser(new User("pochta@mail.ru", "Valera"));
        user2 = userServiceJPA.saveUser(new User("pochta2@mail.ru", "Valera2"));
        item = new Item("Стул", "на четырех ножках");
        item2 = new Item("Стул2", "на четырех ножках 2");
        item.setAvailable(true);
        item2.setAvailable(true);
        itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        itemDto2 = itemServiceJPA.addNewItem(user.getId(), item2);
        bookingDto = new BookingDto(1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemDto.getId());
        bookingDto2 = new BookingDto(2, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemDto2.getId());
    }

    @Test
    void addBooking() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);

        assertThat(newBooking.getId(), notNullValue());
        assertThat(newBooking.getStart(), notNullValue());
        assertThat(newBooking.getEnd(), notNullValue());
        assertThat(newBooking.getItem().getName(), equalTo("Стул"));
        assertThat(newBooking.getItem().getDescription(), equalTo("на четырех ножках"));
        assertThat(newBooking.getItem().getAvailable(), equalTo(true));
        assertThat(newBooking.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void setStatus() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        Booking newBookingSet = bookingServiceJPA.setStatus(user.getId(),
                newBooking.getId(), true);

        assertThat(newBookingSet.getId(), notNullValue());
        assertThat(newBookingSet.getStart(), notNullValue());
        assertThat(newBookingSet.getEnd(), notNullValue());
        assertThat(newBookingSet.getItem().getName(), equalTo("Стул"));
        assertThat(newBookingSet.getItem().getDescription(), equalTo("на четырех ножках"));
        assertThat(newBookingSet.getItem().getAvailable(), equalTo(true));
        assertThat(newBookingSet.getStatus(), equalTo(BookingStatus.APPROVED));

        Booking newBooking2 = bookingServiceJPA.addBooking(user2.getId(), bookingDto2);
        newBookingSet = bookingServiceJPA.setStatus(user.getId(),
                newBooking2.getId(), false);

        assertThat(newBookingSet.getStatus(), equalTo(BookingStatus.REJECTED));
    }

    @Test
    void getById() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingServiceJPA.getById(100500, user.getId());
        });
        assertThat(thrown.getMessage(), equalTo("Такого бронирования нет."));
        Booking booking3 = bookingServiceJPA.getById(newBooking.getId(), user.getId());

        assertThat(booking3.getId(), notNullValue());
        assertThat(booking3.getStart(), notNullValue());
        assertThat(booking3.getEnd(), notNullValue());
        assertThat(booking3.getItem().getName(), equalTo("Стул"));
        assertThat(booking3.getItem().getDescription(), equalTo("на четырех ножках"));
        assertThat(booking3.getItem().getAvailable(), equalTo(true));
        assertThat(booking3.getStatus(), equalTo(BookingStatus.WAITING));
        Booking booking4 = bookingServiceJPA.getById(newBooking.getId(), user2.getId());
        assertThat(booking4.getId(), notNullValue());
        assertThat(booking4.getStart(), notNullValue());
        assertThat(booking4.getEnd(), notNullValue());
        assertThat(booking4.getItem().getName(), equalTo("Стул"));
        assertThat(booking4.getItem().getDescription(), equalTo("на четырех ножках"));
        assertThat(booking4.getItem().getAvailable(), equalTo(true));
        assertThat(booking4.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void getById_2() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        Booking booking3 = bookingServiceJPA.getById(newBooking.getId());

        assertThat(booking3.getId(), notNullValue());
        assertThat(booking3.getStart(), notNullValue());
        assertThat(booking3.getEnd(), notNullValue());
        assertThat(booking3.getItem().getName(), equalTo("Стул"));
        assertThat(booking3.getItem().getDescription(), equalTo("на четырех ножках"));
        assertThat(booking3.getItem().getAvailable(), equalTo(true));
        assertThat(booking3.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void getBookings() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        List<Booking> bookingList = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.ALL);
        assertThat(bookingList.isEmpty(), equalTo(false));
        List<Booking> bookingList2 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.PAST);
        assertThat(bookingList2.isEmpty(), equalTo(true));
        List<Booking> bookingList3 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.CURRENT);
        assertThat(bookingList3.isEmpty(), equalTo(true));
        List<Booking> bookingList4 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.FUTURE);
        assertThat(bookingList4.isEmpty(), equalTo(false));
        List<Booking> bookingList5 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.WAITING);
        assertThat(bookingList5.isEmpty(), equalTo(false));
        List<Booking> bookingList6 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.REJECTED);
        assertThat(bookingList6.isEmpty(), equalTo(true));
    }

    @Test
    void getBookingsPage() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        List<Booking> bookingList = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.ALL, 0, 1);
        assertThat(bookingList.isEmpty(), equalTo(false));
        List<Booking> bookingList2 = bookingServiceJPA.getBookings(user2.getId(),
                RequestParameters.PAST, 0, 1);
        assertThat(bookingList2.isEmpty(), equalTo(true));
        List<Booking> bookingList3 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.CURRENT,
                0, 1);
        assertThat(bookingList3.isEmpty(), equalTo(true));
        List<Booking> bookingList4 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.FUTURE,
                0, 1);
        assertThat(bookingList4.isEmpty(), equalTo(false));
        List<Booking> bookingList5 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.WAITING,
                0, 1);
        assertThat(bookingList5.isEmpty(), equalTo(false));
        List<Booking> bookingList6 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.REJECTED,
                0, 1);
        assertThat(bookingList6.isEmpty(), equalTo(true));

        Throwable thrown2 = assertThrows(ConflictException.class, () -> {
            bookingServiceJPA.getBookings(user2.getId(), RequestParameters.REJECTED,
                    -1, 1);
        });
        assertThat(thrown2.getMessage(),
                equalTo("Параметр from не может быть отрицательным."));

        Throwable thrown3 = assertThrows(ConflictException.class, () -> {
            bookingServiceJPA.getBookings(user2.getId(), RequestParameters.REJECTED,
                    0, -1);
        });
        assertThat(thrown3.getMessage(),
                equalTo("Параметр size должен быть меньше нуля."));

    }


    @Test
    void getBookingsOwner() {
        User newUser = userServiceJPA.saveUser(new User("pochta3@mail.ru", "Valera3"));

        Booking newBooking = bookingServiceJPA.addBooking(newUser.getId(), bookingDto);
        List<Booking> bookings = bookingServiceJPA.getBookingsOwner(newUser.getId(), RequestParameters.ALL);
        assertThat(bookings.isEmpty(), equalTo(true));
        List<Booking> bookings2 = bookingServiceJPA.getBookingsOwner(user.getId(), RequestParameters.ALL);
        assertThat(bookings2.isEmpty(), equalTo(false));
    }

    @Test
    void getBookingsOwnerPage() {
        User newUser = userServiceJPA.saveUser(new User("pochta3@mail.ru", "Valera3"));

        Booking newBooking = bookingServiceJPA.addBooking(newUser.getId(), bookingDto);
        List<Booking> bookings = bookingServiceJPA.getBookingsOwner(newUser.getId(),
                RequestParameters.ALL, 0, 1);
        assertThat(bookings.isEmpty(), equalTo(true));
        List<Booking> bookings2 = bookingServiceJPA.getBookingsOwner(user.getId(),
                RequestParameters.ALL, 0, 1);
        assertThat(bookings2.isEmpty(), equalTo(false));
    }

    @Test
    void lastBooking() {
        Item itemLastBooking = new Item(itemDto.getId());
        Optional<Booking> optionalBooking = bookingServiceJPA.lastBooking(itemLastBooking);
        assertThat(optionalBooking.isEmpty(), equalTo(true));
    }

    @Test
    void nextBooking() {
        Item itemNextBooking = new Item(itemDto.getId());
        Optional<Booking> optionalBooking = bookingServiceJPA.nextBooking(itemNextBooking);
        assertThat(optionalBooking.isEmpty(), equalTo(true));
    }
}