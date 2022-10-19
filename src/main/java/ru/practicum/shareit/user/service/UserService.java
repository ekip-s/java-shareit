package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import javax.validation.Valid;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getById(Long userId);
    User saveUser(@Valid User user);
    User updateUser(User user, Long userId);
    void deleteUser(Long id);
}