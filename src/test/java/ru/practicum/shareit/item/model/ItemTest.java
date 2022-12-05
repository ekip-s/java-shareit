package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void getId() {
        new Item("name", "description");
        new Item("name", "description", 1L);
    }
}