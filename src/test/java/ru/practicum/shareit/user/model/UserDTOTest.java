package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserDTOTest {

    private final String NAME = "name";

    @Test
    void toUserDTOTest() {
        User user = new User(1L, "test@mail.ru", NAME, new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
        UserDTO userDTO = new UserDTO().toUserDTO(user);

        assertThat(userDTO.getId(), equalTo(1L));
        assertThat(userDTO.getName(), equalTo(NAME));
    }
}