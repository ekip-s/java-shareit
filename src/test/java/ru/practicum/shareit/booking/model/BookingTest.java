package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class BookingTest {

    @Test
    void truncateResponse() {
        Booking booking = new Booking(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(6),
                new Item("name", "description"), new User(1L), BookingStatus.WAITING);
        booking = booking.truncateResponse();
        assertThat(booking.getBooker().getEmail(), equalTo(""));
    }
}