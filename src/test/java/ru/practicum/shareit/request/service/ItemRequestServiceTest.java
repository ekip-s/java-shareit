package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceTest {

    private final UserServiceJPA userServiceJPA;
    private final ItemRequestService itemRequestService;
    private ItemRequest itemRequest;
    private ItemRequest itemRequest2;
    private User user;
    private final String string = "такой запрос";
    private final String description2 = "еще один запрос";
    private final String description3 = "Это самый новый запрос.";

    @BeforeEach
    void createTest() {
        itemRequest = new ItemRequest(string);
        itemRequest2 = new ItemRequest(description2);
        user = userServiceJPA.saveUser(new User("pochta@mail.ru", "Valera"));
    }

    @Test
    void addNewRequestTest() {
        ItemRequestDto itemRequestDto = itemRequestService.addNewRequest(itemRequest, user.getId());
        assertThat(itemRequestDto.getId(), notNullValue());
        assertThat(itemRequestDto.getCreated(), notNullValue());
        assertThat(itemRequestDto.getDescription(), equalTo(string));
        assertThat(itemRequestDto.getRequestAuthor().getId(), equalTo(user.getId()));
    }

    @Test
    void myRequestTest() {
        ItemRequestDto itemRequestDto1 = itemRequestService.addNewRequest(itemRequest, user.getId());
        boolean isRequest1 = false;
        ItemRequestDto itemRequestDto2 = itemRequestService.addNewRequest(itemRequest2, user.getId());
        boolean isRequest2 = false;

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.myRequest(user.getId());
        for (ItemRequestDto i : itemRequestDtoList) {
            if (i.getId() == itemRequestDto1.getId()) {
                assertThat(itemRequestDto1.getDescription(), equalTo(string));
                isRequest1 = true;
            }
            if (i.getId() == itemRequestDto2.getId()) {
                assertThat(itemRequestDto2.getDescription(), equalTo(description2));
                isRequest2 = true;
            }
        }
        assertThat(isRequest1, equalTo(true));
        assertThat(isRequest2, equalTo(true));
    }

    @Test
    void strangerRequestTest() {
        User userNew = userServiceJPA.saveUser(new User("pochta2@mail.ru", "Valeria"));
        ItemRequestDto itemRequestDto1 = itemRequestService.addNewRequest(itemRequest, user.getId());
        boolean isRequest1 = false;
        ItemRequest itemRequestNew = new ItemRequest(description3);
        ItemRequestDto itemRequestDto2 = itemRequestService.addNewRequest(itemRequestNew, user.getId());
        boolean isRequest2 = false;

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.strangerRequest(userNew.getId());
        assertThat(itemRequestDtoList.isEmpty(), equalTo(false));

        for (ItemRequestDto i : itemRequestDtoList) {
            if (i.getId() == itemRequestDto1.getId()) {
                assertThat(i.getDescription(), equalTo(string));
                isRequest1 = true;
            }
            if (i.getId() == itemRequestDto2.getId()) {
                assertThat(i.getDescription(), equalTo(description3));
                isRequest2 = true;
            }
        }
        assertThat(isRequest1, equalTo(true));
        assertThat(isRequest2, equalTo(true));
    }

    @Test
    void strangerRequestPageTest() {
        User userNew = userServiceJPA.saveUser(new User("pochta2@mail.ru", "Valeria"));
        ItemRequestDto itemRequestDto1 = itemRequestService.addNewRequest(itemRequest, user.getId());
        boolean isRequest1 = false;
        ItemRequest itemRequestNew = new ItemRequest(description3);
        ItemRequestDto itemRequestDto2 = itemRequestService.addNewRequest(itemRequestNew, user.getId());
        boolean isRequest2 = false;

        List<ItemRequestDto> itemRequestDtoList = itemRequestService.strangerRequest(userNew.getId(), 0, 15);
        assertThat(itemRequestDtoList.isEmpty(), equalTo(false));

        for (ItemRequestDto i : itemRequestDtoList) {
            if (i.getId() == itemRequestDto1.getId()) {
                assertThat(i.getDescription(), equalTo(string));
                isRequest1 = true;
            }
            if (i.getId() == itemRequestDto2.getId()) {
                assertThat(i.getDescription(), equalTo(description3));
                isRequest2 = true;
            }
        }
        assertThat(isRequest1, equalTo(true));
        assertThat(isRequest2, equalTo(true));
    }

    @Test
    void requestByIdTest() {
        ItemRequestDto itemRequestDto1 = itemRequestService.addNewRequest(itemRequest, user.getId());
        Throwable thrown = assertThrows(ConflictException.class, () -> {
            itemRequestService.requestById(100500, itemRequestDto1.getId());
        });
        assertThat(thrown.getMessage(), equalTo("Нет пользователя с id = 100500."));
        Throwable thrown2 = assertThrows(ConflictException.class, () -> {
            itemRequestService.requestById(user.getId(), 100500);
        });
        assertThat(thrown2.getMessage(), equalTo("Нет запроса с id = 100500."));

        ItemRequestDto itemRequestDto2 = itemRequestService.requestById(user.getId(), itemRequestDto1.getId());
        assertThat(itemRequestDto1.getId(), equalTo(itemRequestDto2.getId()));
        assertThat(itemRequestDto2.getDescription(), equalTo(string));
    }
}