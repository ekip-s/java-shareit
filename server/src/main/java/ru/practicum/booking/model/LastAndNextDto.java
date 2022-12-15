package ru.practicum.booking.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LastAndNextDto {
    private long id;
    private long bookerId;

    public LastAndNextDto getLastAndNextDto(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBooker().getId();
        return this;
    }

    public long getId() {
        return id;
    }

    public long getBookerId() {
        return bookerId;
    }
}
