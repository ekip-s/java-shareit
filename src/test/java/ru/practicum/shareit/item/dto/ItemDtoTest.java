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

    private Item item;
    private final String NAME = "Табуретка";
    private final String USER_NAME = "name_user";

    @BeforeEach
    void createTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "описание", LocalDateTime.now(), new ArrayList<>(),
                new User(2L));
        item = new Item(1L, NAME, "табуретка на 4-ех ножках", true, itemRequest,
                new User("test@mail.ru", USER_NAME), new ArrayList<>(), new ArrayList<>());
        new ItemDto(2L, NAME, "табуретка на 4-ех ножках",
                true, new UserDTO(1L, "name"), 1);
    }

    @Test
    void toItemDtoTest() {
        ItemDto itemDto = new ItemDto().toItemDto(item);

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo(NAME));
        assertThat(itemDto.getOwner().getName(), equalTo(USER_NAME));
    }

    @Test
    void toItemDto2Test() {
        ItemDto itemDto = new ItemDto().toItemDto(item, Optional. empty(),
                Optional. empty(), new ArrayList<>());

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo(NAME));
        assertThat(itemDto.getOwner().getName(), equalTo(USER_NAME));
    }

    @Test
    void toItemDto3Test() {
        ItemDto itemDto = new ItemDto().toItemDto(item, new ArrayList<>());

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo(NAME));
        assertThat(itemDto.getOwner().getName(), equalTo(USER_NAME));
    }
}