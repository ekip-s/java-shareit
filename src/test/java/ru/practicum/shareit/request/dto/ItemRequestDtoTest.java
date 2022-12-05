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

    @Test
    void toItemRequestDto() {
        ItemRequest itemRequest = new ItemRequest(1L, "такая информация",
                LocalDateTime.now(), new ArrayList<>(), new User(1L));
        ItemRequestDto itemRequestDto = new ItemRequestDto().toItemRequestDto(itemRequest);
        System.out.println(itemRequestDto);
        assertThat(itemRequestDto.getId(), equalTo(1L));
        assertThat(itemRequestDto.getDescription(), equalTo("такая информация"));
        assertThat(itemRequestDto.getCreated(), notNullValue());
        assertThat(itemRequestDto.getRequestAuthor().getId(), equalTo(1L));
    }
}