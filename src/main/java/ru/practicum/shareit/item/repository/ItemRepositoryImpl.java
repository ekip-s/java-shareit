package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import javax.validation.Valid;
import java.util.*;

@Component
@Validated
public class ItemRepositoryImpl implements ItemRepository {

    private static long id = 0;
    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public Optional<Item> getItemByIdAll(long itemId) {
        return convertToList().stream()
                .filter(g -> Objects.equals(itemId, g.getId()))
                .findAny();
    }

    @Override
    public List<Item> findByUserId(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public Optional<Item> findItemById(long userId, long itemId) {
        return findByUserId(userId).stream()
                .filter(u -> Objects.equals(itemId, u.getId()))
                .findAny();
    }

    @Override
    public Item save(@Valid Item item) {
        item.setId(getId());
        /*items.compute(item.getUserId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });*/
        return item;
    }

    @Override
    public Item update(@Valid Item item) {
        /*items.compute(item.getUserId(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });*/
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (items.containsKey(userId)) {
            List<Item> userItems = items.get(userId);
            userItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> searchList = new ArrayList<>();
        for (List<Item> li: items.values()) {
            li.stream()
                    .filter(Item::getAvailable)
                    .filter(i -> (i.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                            i.getName().toLowerCase().contains(text.toLowerCase())))
                    .forEach(searchList::add);
        }
        return searchList;
    }

    private long getId() {
        return ++id;
    }

    private List<Item> convertToList() {
        List<Item> allItem = new ArrayList<>();
        items.values()
                .forEach(allItem::addAll);
        return allItem;
    }
}