package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@Validated
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking addBooking(@RequestHeader(SHARER_USER_ID) Long userId,
                              @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking setStatus(@RequestHeader(SHARER_USER_ID) Long userId,
                            @PathVariable Long bookingId,@RequestParam Boolean approved) {
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
                                         String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingsOwner(@RequestHeader(SHARER_USER_ID) Long userId,
                                     @RequestParam(required = false, defaultValue = "ALL")
                                     String state) {
        return bookingService.getBookingsOwner(userId, state);
    }
}
