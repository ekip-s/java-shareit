package ru.practicum.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.service.BookingService;
import ru.practicum.booking.model.RequestParameters;
import ru.practicum.booking.model.Booking;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public Booking addBooking(@RequestHeader(SHARER_USER_ID) Long userId,
                              @RequestBody BookingDto bookingDto) {
        log.info("Получен POST запрос к эндпоинту: '/bookings', Строка параметров запроса: {}", bookingDto.toString());
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking setStatus(@RequestHeader(SHARER_USER_ID) Long userId,
                             @PathVariable Long bookingId,@RequestParam Boolean approved) {
        log.info("Получен PATCH запрос к эндпоинту: '/bookings', Строка параметров запроса:" +
                " userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingService.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(SHARER_USER_ID) Long userId,
                                  @PathVariable Long bookingId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping()
    public List<Booking> getBookings(@RequestHeader(SHARER_USER_ID) Long userId,
                                     @RequestParam(required = false, defaultValue = "ALL")
                                     RequestParameters state,
                                     @RequestParam(required = false) Integer from,
                                     @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return bookingService.getBookings(userId, state);
        } else {
            return bookingService.getBookings(userId, state, from, size);
        }
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsOwner(@RequestHeader(SHARER_USER_ID) Long userId,
                                          @RequestParam(required = false, defaultValue = "ALL")
                                          RequestParameters state,
                                          @RequestParam(required = false) Integer from,
                                          @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return bookingService.getBookingsOwner(userId, state);
        } else {
            return bookingService.getBookingsOwner(userId, state, from, size);
        }
    }
}
