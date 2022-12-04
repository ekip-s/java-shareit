package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {

    ItemDto addNewItem(long userId, @Valid Item item);

    List<ItemDto> getItems(long userId,int from, int size);

    List<ItemDto> searchItem(long userId, String text, int from, int size);

    Item updateItem(long userId, Item item, long itemId);

    ItemDto getById(long userId, long itemId);

    List<ItemDto> getItems(long userId);

    void deleteItem(long userId, long itemId);

    List<ItemDto> searchItem(long userId, String text);

    CommentDto addComment(long userId, long itemId, Comment comment);
}
