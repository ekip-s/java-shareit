package ru.practicum.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepositoryJPA extends JpaRepository<Comment, Long> {

    List<Comment> findByItemOrderByCreated(Item item);
}
