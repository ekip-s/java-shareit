package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;
import java.util.List;
import java.util.Optional;

@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class UserServiceJPA implements UserService {

    private final UserRepositoryJPA userRepositoryJPA;

    @Autowired
    public UserServiceJPA(UserRepositoryJPA userRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryJPA.findAll();
    }

    @Override
    public User getById(Long userId) {
        Optional<User> user = userRepositoryJPA.findById(userId);
        checkEntity(user, userId);
        return user.get();
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepositoryJPA.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, Long userId) {
        User initialUser = getById(userId);
        if (user.getName() != null) {
            initialUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            initialUser.setEmail(user.getEmail());
        }
        return userRepositoryJPA.save(initialUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepositoryJPA.deleteById(id);
    }

    private void checkEntity(Optional<User> user, Long userId) {
        if (user.isEmpty())
            throw new IllegalArgumentException("Нет пользователя с id =" + userId);
    }

    private void checkEmail(String email) {
        if (!userRepositoryJPA.findByEmail(email).isEmpty())
            throw new ConflictException("Email указан у другого пользователя");
    }
}
