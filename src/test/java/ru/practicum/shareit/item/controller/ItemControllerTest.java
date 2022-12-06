package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserDTO;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private List<ItemDto> itemDtoList;
    private Item item;
    private CommentDto commentDto;
    private Comment comment;
    private String SharerUserId = "X-Sharer-User-Id";
    private final String NAME  = "Табуретка";
    private final String NAME_2  = "Стул";
    private final String DESCRIPTION = "красивая табуретка";
    private final String DESCRIPTION_2 = "а стул круче";
    private final String DESCRIPTION_3 = "Еще одна табуретка";
    private final String USER_NAME = "Олег";
    private final String USER_NAME_2 = "Виталя";
    private final String USER_NAME_3 = "Троль";
    private final String COMMENT = "Такой коммент";
    private final String URL = "/items";

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());

        itemDto = new ItemDto(1L, NAME, DESCRIPTION, true,
                new UserDTO(1L, USER_NAME), 3);
        itemDto2 = new ItemDto(2L, NAME_2, DESCRIPTION_2, false,
                new UserDTO(2L, USER_NAME_2), 4);
        item = new Item(NAME,DESCRIPTION_3);
        commentDto = new CommentDto(1, COMMENT, USER_NAME_3, LocalDateTime. now());
        comment = new Comment(COMMENT);
        itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        itemDtoList.add(itemDto2);
    }

    @Test
    void getItemsListTest() throws Exception {
        when(itemService.getItems(anyLong()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(URL)
                .header(SharerUserId, 1)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(NAME, NAME_2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION, DESCRIPTION_2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(USER_NAME, USER_NAME_2)));
    }

    @Test
    void getItemsListPageTest() throws Exception {
        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(URL + "?from=0&size=10")
                        .header(SharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(NAME, NAME_2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION, DESCRIPTION_2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(USER_NAME, USER_NAME_2)));
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo(USER_NAME)));
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addNewItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo(USER_NAME)));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(item);

        mockMvc.perform(patch(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(NAME)))
                .andExpect(jsonPath("$.description", equalTo(DESCRIPTION_3)));
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItem(anyLong(), anyString()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(URL + "/search?text=стул")
                        .header(SharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(NAME, NAME_2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION, DESCRIPTION_2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(USER_NAME, USER_NAME_2)));
    }

    @Test
    void searchItemPageTest() throws Exception {
        when(itemService.searchItem(anyLong(), anyString(), anyInt(),anyInt()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(URL + "/search?text=стул&from=0&size=10")
                        .header(SharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(NAME, NAME_2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION, DESCRIPTION_2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(USER_NAME, USER_NAME_2)));
    }

    @Test
    void deleteItemTest() throws Exception {
        mockMvc.perform(delete(URL + "/1")
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk());
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post(URL + "/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(SharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.text", equalTo(COMMENT)))
                .andExpect(jsonPath("$.authorName", equalTo(USER_NAME_3)));
    }
}