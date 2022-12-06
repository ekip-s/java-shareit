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
import ru.practicum.shareit.request.service.ItemRequestService;
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

    private final ItemServiceJPA itemServiceJPA;
    private final UserServiceJPA userServiceJPA;
    private final ItemRequestService itemRequestService;

    private User user;
    private Item item;
    private final String STOOL = "Табуретка";
    private final String STOOL_2 = "Стул";
    private final String STOOL_99 = "Табуретка99";
    private final String STOOL_666 = "Табуретка666";
    private final String DESCRIPTION = "табуретка на 4-ех ножках";
    private final String DESCRIPTION_2 = "Ваза в вИзаНтиЙсКом стиле";
    private final String ERROR = "Нет товара с таким id.";
    private final String VASE = "Ваза";
    private final String SEARCH_QUERY = "византий";

    @BeforeEach
    void createItemTest() {
        user = new User("test@mail.ru", "name_user");
        item = new Item(STOOL, DESCRIPTION);
        userServiceJPA.saveUser(user);
    }

    @Test
    void getItemsTest() {
        Item itemTest99 = new Item(STOOL_99, DESCRIPTION);
        itemTest99.setAvailable(true);
        ItemDto itemDto99 = itemServiceJPA.addNewItem(user.getId(), itemTest99);
        boolean isItemDto99 = false;
        Item itemTest666 = new Item(STOOL_666, DESCRIPTION);
        itemTest666.setAvailable(true);
        ItemDto itemDto666 = itemServiceJPA.addNewItem(user.getId(), itemTest666);
        boolean isItemDto666 = false;

        List<ItemDto> itemDtoList = itemServiceJPA.getItems(user.getId());
        for (ItemDto i : itemDtoList) {
            if (i.getId() == itemDto99.getId()) {
                assertThat(i.getName(), equalTo(STOOL_99));
                isItemDto99 = true;
            }
            if (i.getId() == itemDto666.getId()) {
                assertThat(i.getName(), equalTo(STOOL_666));
                isItemDto666 = true;
            }
        }
        assertThat(isItemDto99, equalTo(true));
        assertThat(isItemDto666, equalTo(true));
    }

    @Test
    void getItemsPageTest() {
        Item itemTest99 = new Item(STOOL_99, DESCRIPTION);
        itemTest99.setAvailable(true);
        ItemDto itemDto99 = itemServiceJPA.addNewItem(user.getId(), itemTest99);
        boolean isItemDto99 = false;
        Item itemTest666 = new Item(STOOL_666, DESCRIPTION);
        itemTest666.setAvailable(true);
        ItemDto itemDto666 = itemServiceJPA.addNewItem(user.getId(), itemTest666);
        boolean isItemDto666 = false;

        List<ItemDto> itemDtoList = itemServiceJPA.getItems(user.getId(), 0,6);
        for (ItemDto i : itemDtoList) {
            if (i.getId() == itemDto99.getId()) {
                assertThat(i.getName(), equalTo(STOOL_99));
                isItemDto99 = true;
            }
            if (i.getId() == itemDto666.getId()) {
                assertThat(i.getName(), equalTo(STOOL_666));
                isItemDto666 = true;
            }
        }
        assertThat(isItemDto99, equalTo(true));
        assertThat(isItemDto666, equalTo(true));
    }

    @Test
    void getByIdTest() {
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            itemServiceJPA.getById(1,156L);
        });
        assertThat(thrown.getMessage(), equalTo(ERROR));

        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        ItemDto newItem = itemServiceJPA.getById(user.getId(),itemDto.getId());

        assertThat(itemDto.getId(), equalTo(newItem.getId()));
        assertThat(newItem.getName(), equalTo(STOOL));
        assertThat(newItem.getDescription(), equalTo(DESCRIPTION));
        }

    @Test
    void addNewItemTest() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);

        assertThat(itemDto.getOwner().getId(), equalTo(user.getId()));
        assertThat(itemDto.getName(), equalTo(STOOL));
        assertThat(itemDto.getDescription(), equalTo(DESCRIPTION));
        assertThat(itemDto.getAvailable(), equalTo(true));
    }

    @Test
    void updateItemTest() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        item.setName(STOOL_2);
        Item updateItem = itemServiceJPA.updateItem(user.getId(), item, itemDto.getId());
        assertThat(updateItem.getId(), equalTo(itemDto.getId()));
        assertThat(updateItem.getName(), equalTo(STOOL_2));
    }

    @Test
    void deleteItemTest() {
        item.setAvailable(true);
        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), item);
        itemServiceJPA.getById(user.getId(), itemDto.getId());
        itemServiceJPA.deleteItem(user.getId(), itemDto.getId());

        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            itemServiceJPA.getById(user.getId(),itemDto.getId());
        });
        assertThat(thrown.getMessage(), equalTo(ERROR));
    }

    @Test
    void searchItemTest() {
        Item newItem = new Item(VASE,DESCRIPTION_2);
        newItem.setAvailable(true);

        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), newItem);
        List<ItemDto> itemDtoList = itemServiceJPA.searchItem(user.getId(), SEARCH_QUERY);

        assertThat(itemDtoList.size(), equalTo(1));
        assertThat(itemDtoList.get(0).getId(), equalTo(itemDto.getId()));
        assertThat(itemDtoList.get(0).getName(), equalTo(VASE));
        assertThat(itemDtoList.get(0).getDescription(), equalTo(DESCRIPTION_2));
        itemServiceJPA.searchItem(user.getId(), "");
    }

    @Test
    void searchItemPageTest() {
        Item newItem = new Item(VASE,DESCRIPTION_2);
        newItem.setAvailable(true);

        ItemDto itemDto = itemServiceJPA.addNewItem(user.getId(), newItem);
        List<ItemDto> itemDtoList = itemServiceJPA.searchItem(user.getId(), SEARCH_QUERY, 0, 1);

        assertThat(itemDtoList.size(), equalTo(1));
        assertThat(itemDtoList.get(0).getId(), equalTo(itemDto.getId()));
        assertThat(itemDtoList.get(0).getName(), equalTo(VASE));
        assertThat(itemDtoList.get(0).getDescription(), equalTo(DESCRIPTION_2));
        itemServiceJPA.searchItem(user.getId(), "", 0, 1);
    }

    @Test
    void addCommentTest() {
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