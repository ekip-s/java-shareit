package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ItemDTORequestTest {

    Item item;

    @BeforeEach
    void createTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "описание", LocalDateTime.now(), new ArrayList<>(),
                new User(2L));
        item = new Item(1L, "Табуретка", "табуретка на 4-ех ножках", true, itemRequest,
                new User("test@mail.ru", "name_user"), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void toItemDTORequest() {
        ItemDTORequest itemDTORequest = new ItemDTORequest().toItemDTORequest(item);

        assertThat(itemDTORequest.getId(), equalTo(1L));
        assertThat(itemDTORequest.getName(), equalTo("Табуретка"));
        assertThat(itemDTORequest.getOwner().getName(), equalTo("name_user"));
    }
}