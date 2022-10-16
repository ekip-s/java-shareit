package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;


import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public Item getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                        @PathVariable Long itemId) {
        return itemService.getById(userId, itemId);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @RequestBody Item item) {
        log.info("Получен POST запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody Item item, @PathVariable Long itemId) {
        log.info("Получен PATCH запрос к эндпоинту: '/items', Строка параметров запроса: {}", item.toString());
        return itemService.updateItem(userId, item, itemId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestParam(required = false, defaultValue = "") String text) {
        return itemService.searchItem(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        log.info("Получен DELETE запрос к эндпоинту: '/items', Строка параметров запроса: {}",
                "userId=" + userId + ", itemId=" + itemId);
        itemService.deleteItem(userId, itemId);
    }
}
