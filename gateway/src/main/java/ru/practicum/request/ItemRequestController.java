package ru.practicum.request;

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
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addNewRequest(@RequestHeader(SHARER_USER_ID) Long userId,
                                                @RequestBody @Valid ItemRequest itemRequest) {
        log.info("Получен POST запрос к эндпоинту: '/requests', Строка параметров запроса: {}, пользователь: {}",
                itemRequest.toString(), userId);
        idCheck(userId);
        return itemRequestClient.addNewRequest(itemRequest, userId);
    }

    @GetMapping
    public ResponseEntity<Object> myRequest(@RequestHeader(SHARER_USER_ID) Long userId) {
        idCheck(userId);
        return itemRequestClient.myRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> strangerRequest(@RequestHeader(SHARER_USER_ID) Long userId,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false) Integer size) {
        idCheck(userId);
        if (from == null || size == null) {
            return itemRequestClient.strangerRequest(userId);
        } else {
            from = checkPaginationParams(from, size);
            return itemRequestClient.strangerRequest(userId, from, size);
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> requestById(@RequestHeader(SHARER_USER_ID) Long userId,
                                              @PathVariable Long requestId) {
        idCheck(userId);
        idCheck(requestId);
        return itemRequestClient.requestById(userId, requestId);
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