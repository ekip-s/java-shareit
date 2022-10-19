package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    @Email(message="Ошибка валидации: e-mail введен неправильно.")
    @NotNull(message="Ошибка валидации: e-mail не заполнен.")
    private String email;
    @NotBlank(message="Ошибка валидации: имя не заполнено.")
    private String name;
}