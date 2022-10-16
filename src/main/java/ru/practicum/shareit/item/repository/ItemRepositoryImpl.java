package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import javax.print.attribute.standard.OrientationRequested;
import javax.validation.Valid;
import java.util.*;

@Component
@Validated
public class ItemRepositoryImpl implements ItemRepository {
    private static long id = 0;
    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public Optional<Item> getItemByIdAll(long itemId) {
        Optional<Item> item = Optional.empty();
        for (List<Item> i: items.values()) {
            if(!i.isEmpty()) {
                item = i.stream()
                        .filter(g -> Objects.equals(itemId, g.getId()))
                        .findAny();
            }
            if(item.isPresent()) {
                break;
            }
        }
        return item;
    }

    @Override
    public List<Item> findByUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Optional<Item> findItemById(long userId, long itemId) {
        List<Item> itemsList = findByUserId(userId);
        Optional<Item> i;
        if(itemsList.isEmpty()){
            i = Optional.empty();
            return i;
        } else {
            return i = itemsList.stream()
                    .filter(u -> Objects.equals(itemId, u.getId()))
                    .findAny();
        }
    }

    @Override
    public Item save(@Valid Item item) {
        item.setId(getId());
        items.compute(item.getUserId(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(@Valid Item item) {
        items.compute(item.getUserId(), (userId, userItems) -> {
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if(items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchList = new ArrayList<>();
        for (List<Item> li: items.values()) {
            li.stream()
                    .filter(i -> (i.getDescription().toLowerCase().contains(text.toLowerCase()) &&
                    i.getAvailable() != false) ||
                            (i.getName().toLowerCase().contains(text.toLowerCase()) &&
                                    i.getAvailable() != false))
                    .forEach(i -> searchList.add(i));
        }
        return searchList;
    }

    private long getId() {
        return ++id;
    }
}