package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.model.User;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
@Validated
public class UserRepositoryImpl implements UserRepository {
    private List<User> users = new ArrayList<>();
    private static long id = 0;

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public User update(@Valid User user) {
        users.add(user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        users = users
                .stream()
                .filter(user -> !Objects.equals(userId, user.getId()))
                .collect(Collectors.toList());
    }
    @Override
    public Optional<User> getById(Long userId) {
        return users.stream()
                .filter(u -> Objects.equals(userId, u.getId()))
                .findAny();
    }

    private long getId() {
        return ++id;
    }
}
