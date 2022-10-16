package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findByUserId(long userId);
    Optional<Item> findItemById(long userId, long itemId);

    Item save(@Valid Item item);
    Optional<Item> getItemByIdAll(long itemId);

    Item update(@Valid Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);
    List<Item> searchItem(String text);
}