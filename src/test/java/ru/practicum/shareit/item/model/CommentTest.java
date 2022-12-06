package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    private final String commentText = "такой комментарий";

    @Test
    void setItemAndAuthorTest() {
        Comment comment = new Comment(commentText);
        assertThat(comment.getText(), equalTo(commentText));

        comment.setItemAndAuthor(new Item(2L), new User(3L));
        assertThat(comment.getAuthor().getId(), equalTo(3L));
        assertThat(comment.getItem().getId(), equalTo(2L));
        assertThat(comment.getCreated(), notNullValue());
    }
}