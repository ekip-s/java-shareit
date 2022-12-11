package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CommentTest {

    private final String comment = "Такой коммент";

    @Test
    void setItemAndAuthorTest() {
        Comment comment = new Comment(this.comment);
        comment.setItemAndAuthor(new Item(1L), new User(1L));

        assertThat(comment.getText(), equalTo(this.comment));
        assertThat(comment.getItem().getId(), equalTo(1L));
        assertThat(comment.getAuthor().getId(), equalTo(1L));
    }
}