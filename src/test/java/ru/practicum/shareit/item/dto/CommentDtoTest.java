package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class CommentDtoTest {

    @Test
    void toCommentDto() {
        User user = new User("email@mail.ru", "name");
        user.setId(12L);
        Comment comment = new Comment(1, "такой коммент", new Item(1L), user, LocalDateTime.now());
        CommentDto commentDto = new CommentDto().toCommentDto(comment);

        assertThat(commentDto.getId(), equalTo(1L));
        assertThat(commentDto.getText(), equalTo("такой коммент"));
        assertThat(commentDto.getAuthorName(), equalTo("name"));
        assertThat(commentDto.getCreated(), notNullValue());
    }
}