package ru.practicum.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(SHARER_USER_ID) Long userId,
                                             @RequestBody @Valid BookingDto bookingDto) {
        log.info("Получен POST запрос к эндпоинту: '/bookings', Строка параметров запроса: {}", bookingDto.toString());
        idCheck(userId);
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setStatus(@RequestHeader(SHARER_USER_ID) Long userId,
                             @PathVariable Long bookingId,@RequestParam Boolean approved) {
        log.info("Получен PATCH запрос к эндпоинту: '/bookings', Строка параметров запроса:" +
                " userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        idCheck(userId);
        idCheck(bookingId);
        return bookingClient.setStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(SHARER_USER_ID) Long userId,
                                  @PathVariable Long bookingId) {
        idCheck(userId);
        idCheck(bookingId);
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getBookings(@RequestHeader(SHARER_USER_ID) Long userId,
                                     @RequestParam(required = false, defaultValue = "ALL")
                                     RequestParameters state,
                                     @RequestParam(required = false) Integer from,
                                     @RequestParam(required = false) Integer size) {
        idCheck(userId);
        if (from == null || size == null) {
            return bookingClient.getBookings(userId, state);
        } else {
            from = checkPaginationParams(from, size);
            return bookingClient.getBookings(userId, state, from, size);
        }
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestHeader(SHARER_USER_ID) Long userId,
                                          @RequestParam(required = false, defaultValue = "ALL")
                                          RequestParameters state,
                                          @RequestParam(required = false) Integer from,
                                          @RequestParam(required = false) Integer size) {
        idCheck(userId);
        if (from == null || size == null) {
            return bookingClient.getBookingsOwner(userId, state);
        } else {
            from = checkPaginationParams(from, size);
            return bookingClient.getBookingsOwner(userId, state, from, size);
        }
    }

    private void idCheck(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }

    private Integer checkPaginationParams(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Ошибка валидации: параметр from не может быть отрицательным.");
        } else if (size < 1) {
            throw new IllegalArgumentException("Ошибка валидации: параметр size должен быть больше нуля.");
        }
        return from / size;
    }
}
