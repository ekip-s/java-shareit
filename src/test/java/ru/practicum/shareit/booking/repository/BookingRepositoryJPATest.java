package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Import(ShareItApp.class)
class BookingRepositoryJPATest {

    @Autowired
    private BookingRepositoryJPA bookingRepositoryJPA;

    @Test
    @Sql({"/test-schema.sql", "/test-data.sql"})
    void findAllBookingByItemJpql() {
        Page<Booking> bookingPage = bookingRepositoryJPA.findAllBookingByItemJpql(
                new User(2L), PageRequest.of(0, 2));
        assertThat(bookingPage.isEmpty(), equalTo(true));
        Page<Booking> bookingPage2 = bookingRepositoryJPA.findAllBookingByItemJpql(
                new User(1L), PageRequest.of(0, 2));
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    @Sql({"/test-schema.sql", "/test-data.sql"})
    void findCurrentBookingByItem() {
        Page<Booking> bookingPage = bookingRepositoryJPA.findCurrentBookingByItem(
                new User(1L), PageRequest.of(0, 2));
        assertThat(bookingPage.isEmpty(), equalTo(false));
    }

    @Test
    @Sql({"/test-schema.sql", "/test-data.sql"})
    void findPastBookingByItem() {
        Page<Booking> bookingPage = bookingRepositoryJPA.findPastBookingByItem(
                new User(2L),  PageRequest.of(0, 2));
        assertThat(bookingPage.isEmpty(), equalTo(true));
        Page<Booking> bookingPage2 = bookingRepositoryJPA.findPastBookingByItem(
                new User(1L),  PageRequest.of(0, 2));
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    @Sql({"/test-schema.sql", "/test-data.sql"})
    void findFutureBookingByItem() {
        Page<Booking> bookingPage = bookingRepositoryJPA.findFutureBookingByItem(
                new User(2L), PageRequest.of(0, 2));
        assertThat(bookingPage.isEmpty(), equalTo(true));
        Page<Booking> bookingPage2 = bookingRepositoryJPA.findFutureBookingByItem(
                new User(1L),  PageRequest.of(0, 2));
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    @Sql({"/test-schema.sql", "/test-data.sql"})
    void findBookingByItemByStatus() {
        Page<Booking> bookingPage = bookingRepositoryJPA.findBookingByItemByStatus(
                new User(2L), BookingStatus.CANCELED, PageRequest.of(0, 2));
        assertThat(bookingPage.isEmpty(), equalTo(true));
        Page<Booking> bookingPage2 = bookingRepositoryJPA.findBookingByItemByStatus(
                new User(1L), BookingStatus.CANCELED, PageRequest.of(0, 2));
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }
}