package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserDTOTest {

    @Test
    void toUserDTO() {
        User user = new User(1L, "test@mail.ru", "name", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
        UserDTO userDTO = new UserDTO().toUserDTO(user);

        assertThat(userDTO.getId(), equalTo(1L));
        assertThat(userDTO.getName(), equalTo("name"));
    }
}