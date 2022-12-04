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
    ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    private ItemDto itemDto;
    private ItemDto itemDto2;
    private List<ItemDto> itemDtoList;
    private Item item;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());

        itemDto = new ItemDto(1L, "Табуретка", "красивая табуретка", true,
                new UserDTO(1L, "Олег"), 3);
        itemDto2 = new ItemDto(2L, "Стул", "а стул круче", false,
                new UserDTO(2L, "Виталя"), 4);
        item = new Item("Табуретка","Еще одна табуретка");
        commentDto = new CommentDto(1, "Такой коммент", "Троль", LocalDateTime. now());
        comment = new Comment("Такой коммент");
        itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        itemDtoList.add(itemDto2);
    }

    @Test
    void get_items_list() throws Exception {
        when(itemService.getItems(anyLong()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Табуретка", "Стул")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("красивая табуретка",
                        "а стул круче")))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder("Олег",
                        "Виталя")));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Табуретка")))
                .andExpect(jsonPath("$.description", equalTo("красивая табуретка")))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo("Олег")));
    }

    @Test
    void add() throws Exception {
        when(itemService.addNewItem(anyLong(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Табуретка")))
                .andExpect(jsonPath("$.description", equalTo("красивая табуретка")))
                .andExpect(jsonPath("$.available", equalTo(true)))
                .andExpect(jsonPath("$.owner.id", equalTo(1)))
                .andExpect(jsonPath("$.owner.name", equalTo("Олег")));
    }

    @Test
    void update() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(item);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(item))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Табуретка")))
                .andExpect(jsonPath("$.description", equalTo("Еще одна табуретка")));
    }

    @Test
    void searchItem() throws Exception {
        when(itemService.searchItem(anyLong(), anyString()))
                .thenReturn(itemDtoList);

        mockMvc.perform(get("/items/search?text=стул")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Табуретка", "Стул")))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder("красивая табуретка",
                        "а стул круче")))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(true, false)))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[*].owner.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].owner.name", containsInAnyOrder("Олег",
                        "Виталя")));
    }

    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(comment))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.text", equalTo("Такой коммент")))
                .andExpect(jsonPath("$.authorName", equalTo("Троль")));
    }
}