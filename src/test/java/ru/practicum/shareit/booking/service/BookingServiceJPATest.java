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

import java.time.LocalDateTime;
import java.util.List;

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
    private Item item;
    private BookingDto bookingDto;

    @BeforeEach
    void createTest() {
        user = userServiceJPA.saveUser(new User("pochta@mail.ru", "Valera"));
        user2 = userServiceJPA.saveUser(new User("pochta2@mail.ru", "Valera2"));
        item = new Item("Стул", "на четырех ножках");
        item.setAvailable(true);
        itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        bookingDto = new BookingDto(1, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemDto.getId());
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
    }

    @Test
    void getBookings() {
        Booking newBooking = bookingServiceJPA.addBooking(user2.getId(), bookingDto);
        List<Booking> bookingList = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.ALL);
        assertThat(bookingList.isEmpty(), equalTo(false));
        List<Booking> bookingList2 = bookingServiceJPA.getBookings(user2.getId(), RequestParameters.PAST);
        assertThat(bookingList2.isEmpty(), equalTo(true));
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
}