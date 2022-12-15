package ru.practicum.user;

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
@RequestMapping("/users")
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        idCheck(userId);
        return userClient.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveNewUser(@Valid @RequestBody UserDTO user) {
        log.info("Получен POST запрос к эндпоинту: '/users', Строка параметров запроса: {}", user.toString());
        return userClient.saveUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserDTO user, @PathVariable Long userId) {
        log.info("Получен PATCH запрос к эндпоинту: '/users', Строка параметров запроса: {}", user.toString());
        idCheck(userId);
        return userClient.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Получен DELETE запрос к эндпоинту: '/users', userId: {}", userId);
        idCheck(userId);
        return userClient.deleteUser(userId);
    }

    private void idCheck(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("Ошибка валидации: id не может быть меньше 1.");
        }
    }
}
