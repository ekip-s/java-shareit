package ru.practicum.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.service.ItemRequestService;
import ru.practicum.request.model.ItemRequest;


import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String SHARER_USER_ID = "X-Sharer-User-Id";

    private ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addNewRequest(@RequestHeader(SHARER_USER_ID) Long userId,
                                        @RequestBody ItemRequest itemRequest) {
        log.info("Получен POST запрос к эндпоинту: '/requests', Строка параметров запроса: {}, пользователь: {}",
                itemRequest.toString(), userId);
        return itemRequestService.addNewRequest(itemRequest, userId);
    }

    @GetMapping
    public List<ItemRequestDto> myRequest(@RequestHeader(SHARER_USER_ID) Long userId) {
        return itemRequestService.myRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> strangerRequest(@RequestHeader(SHARER_USER_ID) Long userId,
                                                @RequestParam(required = false) Integer from,
                                                @RequestParam(required = false) Integer size) {
        if (from == null || size == null) {
            return itemRequestService.strangerRequest(userId);
        } else {
            return itemRequestService.strangerRequest(userId, from, size);
        }
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto requestById(@RequestHeader(SHARER_USER_ID) Long userId, @PathVariable Long requestId) {
        return itemRequestService.requestById(userId, requestId);
    }

}
