package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private long id;
    @NotBlank(message = "Ошибка валидации: комментарий пустой.")
    private String text;

    public Comment(String text) {
        this.text = text;
    }
}