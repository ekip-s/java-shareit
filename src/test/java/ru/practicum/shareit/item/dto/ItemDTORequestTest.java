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

    private Item item;
    private final String name = "Табуретка";
    private final String userName = "name_user";

    @BeforeEach
    void createTest() {
        ItemRequest itemRequest = new ItemRequest(1L, "описание", LocalDateTime.now(), new ArrayList<>(),
                new User(2L));
        item = new Item(1L, name, "табуретка на 4-ех ножках", true, itemRequest,
                new User("test@mail.ru", userName), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void toItemDTORequestTest() {
        ItemDTORequest itemDTORequest = new ItemDTORequest().toItemDTORequest(item);

        assertThat(itemDTORequest.getId(), equalTo(1L));
        assertThat(itemDTORequest.getName(), equalTo(name));
        assertThat(itemDTORequest.getOwner().getName(), equalTo(userName));
    }
}