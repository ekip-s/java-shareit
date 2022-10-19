package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    User save(User user);
    User update(@Valid User user);
    void delete(Long userId);
    Optional<User> getById(Long userId);
}