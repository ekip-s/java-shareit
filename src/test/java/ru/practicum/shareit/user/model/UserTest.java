package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;


class UserTest {

    @Test
    void testUserModelTest() {
        User user = new User("test@mail.ru", "name");
        new User(1L);

        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(1L));
        user.setItems(itemList);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("comment"));
        List<ItemRequest> requests = new ArrayList<>();
        requests.add(new ItemRequest(1L));
    }
}