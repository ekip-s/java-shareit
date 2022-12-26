package ru.practicum.item;

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
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(SHARER_USER_ID) long userId,
                                      @RequestParam(required = false) Integer from,
                                      @RequestParam(required = false) Integer size) {
        checkId(userId);
        if (from == null || size == null) {
            return itemClient.getItems(userId);
        } else {
            from = checkPaginationParams(from, size);
            return itemClient.getItems(userId, from, size);
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        checkId(userId);
        checkId(itemId);
        return itemClient.getById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(SHARER_USER_ID) Long userId,
                       @RequestBody @Valid Item item) {
        log.info("Получен POST запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        checkId(userId);
        return itemClient.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(SHARER_USER_ID) Long userId,
                       @RequestBody Item item, @PathVariable Long itemId) {
        log.info("Получен PATCH запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        checkId(userId);
        checkId(itemId);
        return itemClient.updateItem(userId, item, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                    @RequestParam(required = false, defaultValue = "") String text,
                                    @RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size) {
        checkId(userId);
        if (from == null || size == null) {
            return itemClient.searchItem(userId, text);
        } else {
            from = checkPaginationParams(from, size);
            return itemClient.searchItem(userId, text, from, size);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        log.info("Получен DELETE запрос к эндпоинту: '/items', Строка параметров запроса: userId={}, itemId={}",
                userId, itemId);
        checkId(userId);
        checkId(itemId);
        return itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid Comment comment) {
        log.info("Получен POST запрос к эндпоинту: '/items/itemId={}/comment'," +
                " Строка параметров запроса: userId={}, comment={}", itemId, userId, comment);
        checkId(userId);
        checkId(itemId);
        return itemClient.addComment(userId, itemId, comment);
    }

    private void checkId(long id) {
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
