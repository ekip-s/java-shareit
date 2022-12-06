package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class ItemTest {

    private final String name = "name";
    private final String description = "такое описание";

    @Test
    void testModel() {
        Item item = new Item(name, description);
        assertThat(item.getName(), equalTo(name));
        assertThat(item.getDescription(), equalTo(description));

        Item item2 = new Item(name, description, 1L);
        assertThat(item2.getName(), equalTo(name));
        assertThat(item2.getDescription(), equalTo(description));
        assertThat(item2.getRequestId().getId(), equalTo(1L));
        Item item3 = new Item(1L);
        assertThat(item3.getId(), equalTo(1L));
        Item item4 = new Item(1L, name, description, true, new ItemRequest(3L),
                new User(2L), new ArrayList<>(), new ArrayList<>());
        assertThat(item4.getId(), equalTo(1L));
        assertThat(item4.getName(), equalTo(name));
        assertThat(item4.getDescription(), equalTo(description));
        assertThat(item4.getAvailable(), equalTo(true));
        assertThat(item4.getRequestId().getId(), equalTo(3L));
        assertThat(item4.getOwner().getId(), equalTo(2L));
        assertThat(item4.getBookings().isEmpty(), equalTo(true));
        assertThat(item4.getComments().isEmpty(), equalTo(true));
    }
}