package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceJPATest {

    private final ItemRepositoryJPA itemRepositoryJPA;
    private final ItemServiceJPA itemServiceJPA;
    private final UserServiceJPA userServiceJPA;

    User user;
    Item item;

    @BeforeEach
    void createTest() {
        user = new User("test@mail.ru", "name_user");
        item = new Item("Табуретка", "табуретка на 4-ех ножках");
        userServiceJPA.saveUser(user);
    }

    @Test
    void getItems() {
        Item itemTest99 = new Item("Табуретка99", "табуретка на 4-ех ножках");
        itemTest99.setAvailable(true);
        ItemDto itemDto99 = itemServiceJPA.addNewItem(user.getId(), itemTest99);
        boolean isItemDto99 = false;
        Item itemTest666 = new Item("Табуретка666", "табуретка на 4-ех ножках");
        itemTest666.setAvailable(true);
        ItemDto itemDto666 = itemServiceJPA.addNewItem(user.getId(), itemTest666);
        boolean isItemDto666 = false;

        List<ItemDto> itemDtoList = itemServiceJPA.getItems(user.getId());
        for (ItemDto i : itemDtoList) {
            if (i.getId() == itemDto99.getId()) {
                assertThat(i.getName(), equalTo("Табуретка99"));
                isItemDto99 = true;
            }
            if (i.getId() == itemDto666.getId()) {
                assertThat(i.getName(), equalTo("Табуретка666"));
                isItemDto666 = true;
            }
        }
        assertThat(isItemDto99, equalTo(true));
        assertThat(isItemDto666, equalTo(true));
    }

    @Test
    void getById() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            itemServiceJPA.getById(1,156L);
        });
        assertThat(thrown.getMessage(), equalTo("Нет товара с таким id."));

        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        ItemDto newItem = itemServiceJPA.getById(user.getId(),itemDto.getId());

        assertThat(itemDto.getId(), equalTo(newItem.getId()));
        assertThat(newItem.getName(), equalTo("Табуретка"));
        assertThat(newItem.getDescription(), equalTo("табуретка на 4-ех ножках"));
        }

    @Test
    void addNewItem() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);

        assertThat(itemDto.getOwner().getId(), equalTo(user.getId()));
        assertThat(itemDto.getName(), equalTo("Табуретка"));
        assertThat(itemDto.getDescription(), equalTo("табуретка на 4-ех ножках"));
        assertThat(itemDto.getAvailable(), equalTo(true));
    }

    @Test
    void updateItem() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        item.setName("Стул");
        Item updateItem = itemServiceJPA.updateItem(user.getId(), item, itemDto.getId());
        assertThat(updateItem.getId(), equalTo(itemDto.getId()));
        assertThat(updateItem.getName(), equalTo("Стул"));
    }

    @Test
    void deleteItem() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        itemServiceJPA.getById(user.getId(), itemDto.getId());
        itemServiceJPA.deleteItem(user.getId(), itemDto.getId());

        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            itemServiceJPA.getById(user.getId(),itemDto.getId());
        });
        assertThat(thrown.getMessage(), equalTo("Нет товара с таким id."));
    }

    @Test
    void searchItem() {
        Item newItem = new Item("Ваза","Ваза в вИзаНтиЙсКом стиле");
        newItem.setAvailable(true);

        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), newItem);
        List<ItemDto> itemDtoList = itemServiceJPA.searchItem(user.getId(), "византий");

        assertThat(itemDtoList.size(), equalTo(1));
        assertThat(itemDtoList.get(0).getId(), equalTo(itemDto.getId()));
        assertThat(itemDtoList.get(0).getName(), equalTo("Ваза"));
        assertThat(itemDtoList.get(0).getDescription(), equalTo("Ваза в вИзаНтиЙсКом стиле"));
    }

    @Test
    void addComment() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);

        Throwable thrown = assertThrows(ConflictException.class, () -> {
            itemServiceJPA.addComment(user.getId(),
                    itemDto.getId(), new Comment("Очень все круто."));
        });
        assertThat(thrown.getMessage(),
                equalTo("Отзыв можно оставить только на товар, который бронировался ранее."));
    }
}