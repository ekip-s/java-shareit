package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Validated
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        userExistenceCheck(userId);
        return ItemDtoList(repository.findByUserId(userId));
    }

    private List<ItemDto> ItemDtoList(List<Item> items) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item i: items) {
            itemDtoList.add(new ItemDto().toItemDto(i, Optional.empty(), Optional.empty(),
                    new ArrayList<>()));
        }
        return itemDtoList;
    }


    @Override
    public Item addNewItem(long userId, @Valid Item item) {
        userExistenceCheck(userId);
        //item.setUserId(userId);
        return repository.save(item);
    }

    @Override
    public ItemDto getById(long userId, long itemId) {
        userExistenceCheck(userId);
        Optional<Item> item = repository.getItemByIdAll(itemId);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Нет вещи с id = " + itemId);
        }
        return new ItemDto().toItemDto(item.get(), Optional.empty(), Optional.empty(), new ArrayList<>());
    }

    @Override
    public Item updateItem(long userId, Item item, long itemId) {
        item.setId(itemId);
        userExistenceCheck(userId);
        Item newItem = getByIdInUser(userId, itemId);
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }

        repository.deleteByUserIdAndItemId(userId, item.getId());
        return repository.update(newItem);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<Item> searchItem(long userId, String text) {
        userExistenceCheck(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            return repository.searchItem(text);
        }
    }

    @Override
    public CommentDto addComment(long userId, long itemId, Comment comment) {
        return null;
    }

    private void userExistenceCheck(long id) {
        if (userRepository.getById(id).isEmpty()) {
            throw new IllegalArgumentException("Нет пользователя с id = " + id);
        }
    }

    private Item getByIdInUser(long userId, long itemId) {
        userExistenceCheck(userId);
        Optional<Item> optionalItem = repository.findItemById(userId, itemId);
        if (optionalItem.isEmpty()) {
            throw new IllegalArgumentException("Нет вещи с id = " + itemId);
        } else {
            return optionalItem.get();
        }
    }
}