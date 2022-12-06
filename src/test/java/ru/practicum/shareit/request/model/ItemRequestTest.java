package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class ItemRequestTest {

    private final String description = "такое описание";
    @Test
    void testModel() {
        ItemRequest itemRequest = new ItemRequest(1L);
        assertThat(itemRequest.getId(), equalTo(1L));
        ItemRequest itemRequest2 = new ItemRequest(description);
        assertThat(itemRequest2.getDescription(), equalTo(description));
    }
}