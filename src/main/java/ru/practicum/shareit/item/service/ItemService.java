package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import java.util.List;

public interface ItemService {

    Item addNewItem(long userId, Item item);
    Item updateItem(long userId, Item item, long itemId);
    Item getById(long userId, long itemId);
    List<Item> getItems(long userId);
    void deleteItem(long userId, long itemId);
    List<Item> searchItem(long userId, String text);
}
