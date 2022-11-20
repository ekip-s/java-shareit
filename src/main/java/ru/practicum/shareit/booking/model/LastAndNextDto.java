package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LastAndNextDto {
    private long id;
    private long bookerId;

    public LastAndNextDto getLastAndNextDto(Booking booking) {
        this.id = booking.getId();
        this.bookerId = booking.getBooker().getId();
        return this;
    }
}
