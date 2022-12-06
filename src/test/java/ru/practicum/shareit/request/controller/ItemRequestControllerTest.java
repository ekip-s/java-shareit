package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDto2;
    private List<ItemRequestDto> itemRequestDtoList;
    private String SharerUserId = "X-Sharer-User-Id";
    private final String URL = "/requests";
    private final String REQUEST = "Запрос";
    private final String REQUEST_2 = "Запрос2";
    private final String NAME = "name";
    private final String NAME_2 = "name2";

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());
        itemRequest = new ItemRequest(1L, REQUEST, LocalDateTime. now(),
                new ArrayList<>(), new User("milo@mail.ru", NAME));
        itemRequestDto = new ItemRequestDto(1L, REQUEST, LocalDateTime. now(), new ArrayList<>(),
                new UserDTO(1L, NAME));
        itemRequestDto2 = new ItemRequestDto(2L, REQUEST_2, LocalDateTime. now(), new ArrayList<>(),
                new UserDTO(1L, NAME_2));

        itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(itemRequestDto);
        itemRequestDtoList.add(itemRequestDto2);
    }

    @Test
    void addNewRequestTest() throws Exception {
        when(itemRequestService.addNewRequest(any(), anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(itemRequest))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.description", equalTo(REQUEST)))
                .andExpect(jsonPath("$.requestAuthor.id", equalTo(1)))
                .andExpect(jsonPath("$.requestAuthor.name", equalTo(NAME)));
    }

    @Test
    void myRequestTest() throws Exception {
        when(itemRequestService.myRequest(anyLong()))
                .thenReturn(itemRequestDtoList);

        mockMvc.perform(get(URL)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(REQUEST, REQUEST_2)))
                .andExpect(jsonPath("$[*].requestAuthor.id", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].requestAuthor.name", containsInAnyOrder(NAME, NAME_2)));
    }

    @Test
    void strangerRequestTest() throws Exception {
        when(itemRequestService.strangerRequest(anyLong()))
                .thenReturn(itemRequestDtoList);

        mockMvc.perform(get(URL + "/all")
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(REQUEST, REQUEST_2)))
                .andExpect(jsonPath("$[*].requestAuthor.id", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].requestAuthor.name", containsInAnyOrder(NAME, NAME_2)));
    }

    @Test
    void strangerRequestPageTest() throws Exception {
        when(itemRequestService.strangerRequest(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        mockMvc.perform(get(URL + "/all?from=0&size=10")
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(REQUEST, REQUEST_2)))
                .andExpect(jsonPath("$[*].requestAuthor.id", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$[*].requestAuthor.name", containsInAnyOrder(NAME, NAME_2)));
    }

    @Test
    void requestByIdTest() throws Exception {
        when(itemRequestService.requestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto2);

        mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(jsonPath("$.description", equalTo(REQUEST_2)))
                .andExpect(jsonPath("$.requestAuthor.id", equalTo(1)))
                .andExpect(jsonPath("$.requestAuthor.name", equalTo(NAME_2)));
    }
}