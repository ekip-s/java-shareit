package ru.practicum.user.service;

import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getById(Long userId);

    User saveUser(User user);

    User updateUser(User user, Long userId);

    void deleteUser(Long id);
}
