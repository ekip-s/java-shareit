package ru.practicum.booking.service;

import ru.practicum.booking.model.RequestParameters;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking addBooking(Long userId, BookingDto bookingDto);

    Booking setStatus(long userId, long bookingId, boolean approved);

    Booking getById(long bookingId);

    Booking getById(long bookingId, long userId);

    List<Booking> getBookings(long userId, RequestParameters requestParameters);

    List<Booking> getBookings(long userId, RequestParameters requestParameters, int from, int size);

    List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters, int from, int size);

    List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters);

    Optional<Booking> lastBooking(Item item);

    Optional<Booking> nextBooking(Item item);
}