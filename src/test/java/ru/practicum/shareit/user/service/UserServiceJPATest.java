package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceJPATest {

    private final UserServiceJPA userServiceJPA;
    private User user;
    private final String email = "test@mail.ru";
    private final String email2 = "test156@mail.ru";
    private final String email3 = "test157@mail.ru";
    private final String name = "name_user";
    private final String name2 = "name_user156";
    private final String name3 = "name_user157";
    private final String name4 = "Vitalik";

    @BeforeEach
    void createTest() {
        user = new User(email, name);
    }

    @Test
    void getAllUsersTest() {
        User user156 = userServiceJPA.saveUser(new User(email2, name2));
        User user157 = userServiceJPA.saveUser(new User(email3, name3));

        List<User> users = userServiceJPA.getAllUsers();
        boolean isGoodTest156 = false;
        boolean isGoodTest157 = false;

        for (User u : users) {
            if (u.getId() == user156.getId()) {
                assertThat(u.getName(), equalTo(name2));
                assertThat(u.getEmail(), equalTo(email2));
                isGoodTest156 = true;
            }
            if (u.getId() == user157.getId()) {
                assertThat(u.getName(), equalTo(name3));
                assertThat(u.getEmail(), equalTo(email3));
                isGoodTest157 = true;
            }
        }
        assertThat(isGoodTest156, equalTo(true));
        assertThat(isGoodTest157, equalTo(true));
    }

    @Test
    void getByIdTest() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            userServiceJPA.getById(1L);
        });
        assertThat(thrown.getMessage(), equalTo("Нет пользователя с id =1"));
        User testUser = userServiceJPA.saveUser(user);
        User testUser2 = userServiceJPA.getById(testUser.getId());
        assertThat(testUser2.getName(), equalTo(name));
        assertThat(testUser2.getEmail(), equalTo(email));

        Throwable thrown2 = assertThrows(ConstraintViolationException.class, () -> {
            userServiceJPA.saveUser(new User("testmail.ru", name3));
        });
        assertThat(thrown2.getMessage(),
                equalTo("saveUser.user.email: Ошибка валидации: e-mail введен неправильно."));

        Throwable thrown3 = assertThrows(ConstraintViolationException.class, () -> {
            userServiceJPA.saveUser(new User(email, null));
        });
        assertThat(thrown3.getMessage(),
                equalTo("saveUser.user.name: Ошибка валидации: имя не заполнено."));
    }

    @Test
    void saveUserTest() {
        User user2 = userServiceJPA.saveUser(user);
        assertThat(user2.getId(), notNullValue());
        assertThat(user2.getName(), equalTo(name));
        assertThat(user2.getEmail(), equalTo(email));
    }

    @Test
    void updateUserTest() {
        user = userServiceJPA.saveUser(user);
        user.setName(name4);
        User user2 = userServiceJPA.updateUser(user, user.getId());
        assertThat(user2.getId(), notNullValue());
        assertThat(user2.getName(), equalTo(name4));
        assertThat(user2.getEmail(), equalTo(email));
    }

    @Test
    void deleteUserTest() {
        User user1 = userServiceJPA.saveUser(user);
        User testUser = userServiceJPA.getById(user1.getId());
        assertThat(testUser.getName(), equalTo(name));
        assertThat(testUser.getEmail(), equalTo(email));

        userServiceJPA.deleteUser(user1.getId());
        Throwable thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            userServiceJPA.getById(user1.getId());
        });
        assertThat(thrown2.getMessage(), equalTo("Нет пользователя с id =" + user1.getId()));
    }
}