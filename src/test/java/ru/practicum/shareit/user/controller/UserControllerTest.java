package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private UserServiceJPA userService;
    private User user;
    private User user2;
    private List<User> users;
    private final String url = "/users";
    private final String email = "milo@mail.ru";
    private final String email2 = "milo2@mail.ru";
    private final String name = "name";
    private final String name2 = "name2";

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());
        user = new User(email, name);
        user2 = new User(email2, name2);
        users = new ArrayList<>();
        users.add(user);
        users.add(user2);
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get(url)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder(email, email2)));
    }

    @Test
    void getByIdTest() throws Exception {
        when(userService.getById(anyLong()))
                .thenReturn(user);

        mockMvc.perform(get(url + "/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.email", equalTo(email)));
    }

    @Test
    void saveNewUserTest() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(user);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.email", equalTo(email)));
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(any(), anyLong()))
                .thenReturn(user);

        mockMvc.perform(patch(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.email", equalTo(email)));
    }

    @Test
    void deleteUserTest() throws Exception {
        mockMvc.perform(delete( url + "/1")
                )
                .andExpect(status().isOk());
    }
}