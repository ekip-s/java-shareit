package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Import(ShareItApp.class)
@Sql({"/test-schema.sql", "/test-data.sql"})
class CommentRepositoryJPATest {

    @Autowired
    private CommentRepositoryJPA commentRepositoryJPA;
    private final String COMMENT = "такой комментарий";

    @Test
    void findByItemOrderByCreatedTest() {
        Comment comment = new Comment(COMMENT);
        comment.setItemAndAuthor(new Item(1L), new User(1L));
        commentRepositoryJPA.save(comment);
        List<Comment> commentList = commentRepositoryJPA.findByItemOrderByCreated(new Item(1L));

        assertThat(commentList.get(0).getId(), equalTo(1L));
        assertThat(commentList.get(0).getText(), equalTo(COMMENT));
    }
}