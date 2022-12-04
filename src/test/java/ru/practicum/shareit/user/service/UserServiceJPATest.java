package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
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

    @BeforeEach
    void createTest() {
        user = new User("test@mail.ru", "name_user");
    }

    @Test
    void getAllUsers() {
        User user156 = userServiceJPA.saveUser(new User("test156@mail.ru", "name_user156"));
        User user157 = userServiceJPA.saveUser(new User("test157@mail.ru", "name_user157"));

        List<User> users = userServiceJPA.getAllUsers();
        boolean isGoodTest156 = false;
        boolean isGoodTest157 = false;

        for (User u : users) {
            if (u.getId() == user156.getId()) {
                assertThat(u.getName(), equalTo("name_user156"));
                assertThat(u.getEmail(), equalTo("test156@mail.ru"));
                isGoodTest156 = true;
            }
            if (u.getId() == user157.getId()) {
                assertThat(u.getName(), equalTo("name_user157"));
                assertThat(u.getEmail(), equalTo("test157@mail.ru"));
                isGoodTest157 = true;
            }
        }
        assertThat(isGoodTest156, equalTo(true));
        assertThat(isGoodTest157, equalTo(true));
    }

    @Test
    void getById() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            userServiceJPA.getById(1L);
        });
        assertThat(thrown.getMessage(), equalTo("Нет пользователя с id =1"));
        User testUser = userServiceJPA.saveUser(user);
        User testUser2 = userServiceJPA.getById(testUser.getId());
        assertThat(testUser2.getName(), equalTo("name_user"));
        assertThat(testUser2.getEmail(), equalTo("test@mail.ru"));
    }

    @Test
    void saveUser() {
        User user2 = userServiceJPA.saveUser(user);
        assertThat(user2.getId(), notNullValue());
        assertThat(user2.getName(), equalTo("name_user"));
        assertThat(user2.getEmail(), equalTo("test@mail.ru"));
    }

    @Test
    void updateUser() {
        user = userServiceJPA.saveUser(user);
        user.setName("Vitalik");
        User user2 = userServiceJPA.updateUser(user, user.getId());
        assertThat(user2.getId(), notNullValue());
        assertThat(user2.getName(), equalTo("Vitalik"));
        assertThat(user2.getEmail(), equalTo("test@mail.ru"));
    }

    @Test
    void deleteUser() {
        User user1 = userServiceJPA.saveUser(user);
        User testUser = userServiceJPA.getById(user1.getId());
        assertThat(testUser.getName(), equalTo("name_user"));
        assertThat(testUser.getEmail(), equalTo("test@mail.ru"));

        userServiceJPA.deleteUser(user1.getId());
        Throwable thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            userServiceJPA.getById(user1.getId());
        });
        assertThat(thrown2.getMessage(), equalTo("Нет пользователя с id =" + user1.getId()));
    }
}