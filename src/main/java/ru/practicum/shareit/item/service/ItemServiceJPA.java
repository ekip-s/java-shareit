package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;
import ru.practicum.shareit.user.service.UserServiceJPA;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class ItemServiceJPA implements ItemService {

    ItemRepositoryJPA itemRepositoryJPA;
    UserServiceJPA userServiceJPA;

    @Autowired
    public ItemServiceJPA(ItemRepositoryJPA itemRepositoryJPA, UserServiceJPA userServiceJPA) {
        this.itemRepositoryJPA = itemRepositoryJPA;
        this.userServiceJPA = userServiceJPA;
    }

    @Override
    public List<Item> getItems(long userId) {
        return itemRepositoryJPA.findByOwner(new User(userId));
    }

    @Override
    public Item getById(long userId, long itemId) {
        Optional<Item> item = itemRepositoryJPA.findById(itemId);
        checkEntity(item, itemId);
        return item.get();
    }

    @Transactional
    @Override
    public Item addNewItem(long userId, @Valid Item item) {
        userServiceJPA.getById(userId);
        item.setOwner(new User(userId));
        return itemRepositoryJPA.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(long userId, Item item, long itemId) {
        Item initialItem = getById(userId, itemId);
        checkOwner(userId, initialItem.getOwner().getId());
        if (item.getName() != null) {
            initialItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            initialItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            initialItem.setAvailable(item.getAvailable());
        }
        return itemRepositoryJPA.save(initialItem);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepositoryJPA.deleteById(itemId);
    }

    @Override
    public List<Item> searchItem(long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepositoryJPA.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        }
    }

    private void checkEntity(Optional<Item> item, Long itemId) {
        if (item.isEmpty())
            throw new IllegalArgumentException("Нет товара с id =" + itemId);
    }

    private void checkOwner(long id, long itemUserId) {
        if (id != itemUserId) {
            throw new IllegalArgumentException("Товар принадлежит другому пользователю - нельзя обновить");
        }
    }
}
