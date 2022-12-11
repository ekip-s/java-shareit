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
    private final User user = new User(1L);
    private final User user2 = new User(2L);
    private final Pageable page = PageRequest.of(0, 2);


    @Test
    void findAllBookingByItemJpqlTest() {
        bookingPage = bookingRepositoryJPA.findAllBookingByItemJpql(user2, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findAllBookingByItemJpql(user, page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findCurrentBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findCurrentBookingByItem(user, page);
        assertThat(bookingPage.isEmpty(), equalTo(false));
    }

    @Test
    void findPastBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findPastBookingByItem(user2,  page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findPastBookingByItem(user,  page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findFutureBookingByItemTest() {
        bookingPage = bookingRepositoryJPA.findFutureBookingByItem(user2, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findFutureBookingByItem(user,  page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }

    @Test
    void findBookingByItemByStatusTest() {
        bookingPage = bookingRepositoryJPA.findBookingByItemByStatus(user2, BookingStatus.CANCELED, page);
        assertThat(bookingPage.isEmpty(), equalTo(true));
        bookingPage2 = bookingRepositoryJPA.findBookingByItemByStatus(user, BookingStatus.CANCELED, page);
        assertThat(bookingPage2.isEmpty(), equalTo(false));
    }
}