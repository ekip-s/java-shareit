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
    private String sharerUserId = "X-Sharer-User-Id";
    private final String name = "Табуретка";
    private final String name2 = "Стул";
    private final String description = "красивая табуретка";
    private final String description2 = "а стул круче";
    private final String description3 = "Еще одна табуретка";
    private final String userName = "Олег";
    private final String userName2 = "Виталя";
    private final String userName3 = "Троль";
    private final String comment2 = "Такой коммент";
    private final String url = "/items";

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());

        itemDto = new ItemDto(1L, name, description, true,
                new UserDTO(1L, userName), 3);
        itemDto2 = new ItemDto(2L, name2, description2, false,
                new UserDTO(2L, userName2), 4);
        item = new Item(name, description3);
        commentDto = new CommentDto(1, comment2, userName3, LocalDateTime. now());
        comment = new Comment(comment2);
        itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        itemDtoList.add(itemDto2);
    }

    @Test
    void getItemsListTest() throws Exception {
        when(itemService.getItems(anyLong()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(url)
                .header(sharerUserId, 1)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(description, description2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(userName, userName2)));
    }

    @Test
    void getItemsListPageTest() throws Exception {
        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(url + "?from=0&size=10")
                        .header(sharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(description, description2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(userName, userName2)));
    }

    @Test
    void getByIdTest() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo(description)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo(userName)));
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addNewItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo(description)))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo(userName)));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(item);

        mockMvc.perform(patch(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.description", equalTo(description3)));
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.searchItem(anyLong(), anyString()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(url + "/search?text=стул")
                        .header(sharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(description, description2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(userName, userName2)));
    }

    @Test
    void searchItemPageTest() throws Exception {
        when(itemService.searchItem(anyLong(), anyString(), anyInt(),anyInt()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get(url + "/search?text=стул&from=0&size=10")
                        .header(sharerUserId, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(description, description2)))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder(userName, userName2)));
    }

    @Test
    void deleteItemTest() throws Exception {
        mockMvc.perform(delete(url + "/1")
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk());
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post(url + "/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.text", equalTo(comment2)))
                .andExpect(jsonPath("$.authorName", equalTo(userName3)));
    }
}