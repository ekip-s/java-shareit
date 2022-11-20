package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public CommentDto toCommentDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.authorName = comment.getAuthor().getName();
        this.created = comment.getCreated();
        return this;
    }
}
