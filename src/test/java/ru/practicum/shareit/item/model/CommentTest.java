package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CommentTest {

    @Test
    void setItemAndAuthor() {
        Comment comment = new Comment("Такой коммент");
        comment.setItemAndAuthor(new Item(1L), new User(1L));

        assertThat(comment.getText(), equalTo("Такой коммент"));
        assertThat(comment.getItem().getId(), equalTo(1L));
        assertThat(comment.getAuthor().getId(), equalTo(1L));
    }
}