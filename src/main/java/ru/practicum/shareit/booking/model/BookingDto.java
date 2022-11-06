package ru.practicum.shareit.booking.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private long id;
    @Future(message = "Дата должна быть в будущем.")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date start;
    @Future(message = "Дата должна быть в будущем.")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private Date end;
    private long itemId;
}
