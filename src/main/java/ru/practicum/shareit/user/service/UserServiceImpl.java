package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getById(Long userId) {
        userExistenceCheck(userId);
        if (repository.getById(userId).isPresent()) {
            return repository.getById(userId).get();
        } else {
            return new User();
        }
    }

    @Override
    public User saveUser(@Valid User user) {
        entityCheck(user);
        return repository.save(user);
    }

    @Override
    public User updateUser(User user, Long userId) {
        userExistenceCheck(userId);
        if(repository.getById(userId).isPresent()) {
            User oldUser = repository.getById(userId).get();
            if(user.getEmail() != null) {
                entityCheck(user);
                oldUser.setEmail(user.getEmail());
            }
            if(user.getName() != null) {
                oldUser.setName(user.getName());
            }
            repository.delete(userId);
            repository.update(oldUser);
            return oldUser;
        } else {
            return new User();
        }
    }

    @Override
    public void deleteUser(Long userId) {
        repository.delete(userId);
    }

    private void entityCheck(@Valid User user) {
        if(!getAllUsers().isEmpty()) {
            for(User u: getAllUsers()) {
                if(u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId())) {
                    throw new ConflictException("Ошибка валидации: email уже используется.");
                }
            }
        }
    }

    private void userExistenceCheck(long id) {
        if(repository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Нет пользователя с id = " + id);
        }
    }
}