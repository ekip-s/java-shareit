package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class ItemRequestDtoTest {

    private final String DESCRIPTION = "такая информация";

    @Test
    void toItemRequestDtoTest() {
        ItemRequest itemRequest = new ItemRequest(1L, DESCRIPTION,
                LocalDateTime.now(), new ArrayList<>(), new User(1L));
        ItemRequestDto itemRequestDto = new ItemRequestDto().toItemRequestDto(itemRequest);
        System.out.println(itemRequestDto);
        assertThat(itemRequestDto.getId(), equalTo(1L));
        assertThat(itemRequestDto.getDescription(), equalTo(DESCRIPTION));
        assertThat(itemRequestDto.getCreated(), notNullValue());
        assertThat(itemRequestDto.getRequestAuthor().getId(), equalTo(1L));
    }
}