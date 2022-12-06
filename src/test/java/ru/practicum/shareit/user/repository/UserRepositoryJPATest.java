package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Import(ShareItApp.class)
@Sql({"/test-schema.sql", "/test-data.sql"})
class UserRepositoryJPATest {

    private final String EMAIL = "valera@mail.ru";

    @Autowired
    private UserRepositoryJPA userRepositoryJPA;

    @Test
    void findByEmail() {
        User user = userRepositoryJPA.findByEmail(EMAIL).get(0);

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("ValeraTestJPA"));
        assertThat(user.getEmail(), equalTo(EMAIL));
    }
}