package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class LastAndNextDtoTest {

    @Test
    void getLastAndNextDtoTest() {
        Booking booking = new Booking(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(6),
                new Item("name", "description"), new User(1L), BookingStatus.WAITING);
        LastAndNextDto lastAndNextDto = new LastAndNextDto().getLastAndNextDto(booking);
        assertThat(lastAndNextDto.getId(), equalTo(1L));
        assertThat(lastAndNextDto.getBookerId(), equalTo(1L));
    }
}