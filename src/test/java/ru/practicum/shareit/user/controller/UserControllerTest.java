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

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());
        user = new User("milo@mail.ru", "name");
        user2 = new User("milo2@mail.ru", "name2");
        users = new ArrayList<>();
        users.add(user);
        users.add(user2);
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("name", "name2")))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder("milo@mail.ru", "milo2@mail.ru")));
    }

    @Test
    void getById() throws Exception {
        when(userService.getById(anyLong()))
                .thenReturn(user);

        mockMvc.perform(get("/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.email", equalTo("milo@mail.ru")));
    }

    @Test
    void saveNewUser() throws Exception {
        when(userService.saveUser(any()))
                .thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.email", equalTo("milo@mail.ru")));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(), anyLong()))
                .thenReturn(user);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.email", equalTo("milo@mail.ru")));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1")
                )
                .andExpect(status().isOk());
    }
}