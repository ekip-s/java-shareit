package ru.practicum.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.service.ItemService;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> get(@RequestHeader(SHARER_USER_ID) long userId,
                             @RequestParam(required = false) Integer from,
                             @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return itemService.getItems(userId);
        } else {
            return itemService.getItems(userId, from, size);
        }
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader(SHARER_USER_ID) Long userId,
                       @RequestBody Item item) {
        log.info("Получен POST запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader(SHARER_USER_ID) Long userId,
                       @RequestBody Item item, @PathVariable Long itemId) {
        log.info("Получен PATCH запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        return itemService.updateItem(userId, item, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader(SHARER_USER_ID) Long userId,
                                    @RequestParam(required = false, defaultValue = "") String text,
                                    @RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return itemService.searchItem(userId, text);
        } else {
            return itemService.searchItem(userId, text, from, size);
        }
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(SHARER_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        log.info("Получен DELETE запрос к эндпоинту: '/items', Строка параметров запроса: userId={}, itemId={}",
                userId, itemId);
        itemService.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody Comment comment) {
        log.info("Получен POST запрос к эндпоинту: '/items/itemId={}/comment'," +
                " Строка параметров запроса: userId={}, comment={}", itemId, userId, comment);
        return itemService.addComment(userId, itemId, comment);
    }
}
