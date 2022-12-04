package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ItemDTORequestTest {

    Item item;

    @BeforeEach
    void createTest() {
        item = new Item("Табуретка", "табуретка на 4-ех ножках");
        item.setId(1L);
        item.setOwner(new User("test@mail.ru", "name_user"));
    }

    @Test
    void toItemDTORequest() {
        ItemDTORequest itemDTORequest = new ItemDTORequest().toItemDTORequest(item);

        assertThat(itemDTORequest.getId(), equalTo(1L));
        assertThat(itemDTORequest.getName(), equalTo("Табуретка"));
        assertThat(itemDTORequest.getOwner().getName(), equalTo("name_user"));
    }
}