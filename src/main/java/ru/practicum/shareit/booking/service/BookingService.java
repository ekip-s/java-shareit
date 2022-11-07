package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.controller.RequestParameters;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking addBooking(Long userId, BookingDto bookingDto);

    Booking setStatus(long userId, long bookingId, boolean approved);

    Booking getById(long bookingId);

    Booking getById(long bookingId, long userId);

    List<Booking> getBookings(long userId, RequestParameters requestParameters);

    List<Booking> getBookingsOwner(long userId, RequestParameters requestParameters);

    Optional<Booking> lastBooking(Item item);

    Optional<Booking> nextBooking(Item item);
}
