package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class BookingDtoTest {

    @Test
    void testModel() {
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1), 1);

        assertThat(bookingDto.getId(), equalTo(1L));
        assertThat(bookingDto.getStart(), notNullValue());
        assertThat(bookingDto.getEnd(), notNullValue());
        assertThat(bookingDto.getItemId(), equalTo(1L));
    }
}