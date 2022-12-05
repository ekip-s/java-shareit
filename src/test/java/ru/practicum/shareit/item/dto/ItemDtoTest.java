package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    Item item;

    @BeforeEach
    void createTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "описание", LocalDateTime.now(), new ArrayList<>(),
                new User(2L));
        item = new Item(1L, "Табуретка", "табуретка на 4-ех ножках", true, itemRequest,
                new User("test@mail.ru", "name_user"), new ArrayList<>(), new ArrayList<>());
        new ItemDto(2L, "Табуретка", "табуретка на 4-ех ножках",
                true, new UserDTO(1L, "name"), 1);
    }

    @Test
    void toItemDto() {
        ItemDto itemDto = new ItemDto().toItemDto(item);

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("Табуретка"));
        assertThat(itemDto.getOwner().getName(), equalTo("name_user"));
    }

    @Test
    void testToItemDto() {
        ItemDto itemDto = new ItemDto().toItemDto(item, Optional. empty(),
                Optional. empty(), new ArrayList<>());

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("Табуретка"));
        assertThat(itemDto.getOwner().getName(), equalTo("name_user"));
    }

    @Test
    void testToItemDto1() {
        ItemDto itemDto = new ItemDto().toItemDto(item, new ArrayList<>());

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("Табуретка"));
        assertThat(itemDto.getOwner().getName(), equalTo("name_user"));
    }
}