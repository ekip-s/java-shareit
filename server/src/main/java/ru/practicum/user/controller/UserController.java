package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public User saveNewUser(@RequestBody User user) {
        log.info("Получен POST запрос к эндпоинту: '/users', Строка параметров запроса: {}", user.toString());
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable Long userId) {
        log.info("Получен PATCH запрос к эндпоинту: '/users', Строка параметров запроса: {}", user.toString());
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен DELETE запрос к эндпоинту: '/users', userId: {}", userId);
        userService.deleteUser(userId);
    }
}
