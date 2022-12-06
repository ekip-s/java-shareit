package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Import(ShareItApp.class)
@Sql({"/test-schema.sql", "/test-data.sql"})
class BookingRepositoryJPATest {

    @Autowired
    private BookingRepositoryJPA bookingRepositoryJPA;
    private Page<Booking> bookingPage;
    private Page<Booking> bookingPage2;
    private final User USER = new User(1L);
    private final User USER2 = new User(2L);
    private final Pageable page = PageRequest.of(0, 2);


    @Test
    void findAllBookingByItemJpqlTest() {
        bookingPage = bookingRepositoryJPA.findAllBookingByItemJpql(USER2, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findAllBookingByItemJpql(USER, page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findCurrentBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findCurrentBookingByItem(USER, page);
        assertThat(bookingPage.isEmpty(), equalTo(false));
    }

    @Test
    void findPastBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findPastBookingByItem(USER2,  page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findPastBookingByItem(USER,  page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findFutureBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findFutureBookingByItem(USER2, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findFutureBookingByItem(USER,  page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findBookingByItemByStatusTest() {
        bookingPage = bookingRepositoryJPA.findBookingByItemByStatus(USER2, BookingStatus.CANCELED, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findBookingByItemByStatus(USER, BookingStatus.CANCELED, page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }
}