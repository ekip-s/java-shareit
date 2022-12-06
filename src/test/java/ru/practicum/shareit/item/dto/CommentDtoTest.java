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

    private final String NAME = "name";
    private final String COMMENT = "такой коммент";

    @Test
    void toCommentDtoTest() {
        User user = new User("email@mail.ru", NAME);
        user.setId(12L);
        Comment comment = new Comment(1, COMMENT, new Item(1L), user, LocalDateTime.now());
        CommentDto commentDto = new CommentDto().toCommentDto(comment);

        assertThat(commentDto.getId(), equalTo(1L));
        assertThat(commentDto.getText(), equalTo(COMMENT));
        assertThat(commentDto.getAuthorName(), equalTo(NAME));
        assertThat(commentDto.getCreated(), notNullValue());
    }
}