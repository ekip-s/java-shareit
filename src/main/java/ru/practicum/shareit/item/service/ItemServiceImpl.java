package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
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
    public List<Item> getItems(long userId) {
        userExistenceCheck(userId);
        return repository.findByUserId(userId);
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        userExistenceCheck(userId);
        item.setUserId(userId);
        return repository.save(item);
    }

    @Override
    public Item getById(long userId, long itemId) {
        userExistenceCheck(userId);
        Optional<Item> item = repository.getItemByIdAll(itemId);
        if (item.isEmpty()) {
            throw new IllegalArgumentException("Нет вещи с id = " + itemId);
        }
        return item.get();
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