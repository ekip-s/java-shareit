package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @Email(message = "Ошибка валидации: e-mail введен неправильно.")
    @NotNull(message = "Ошибка валидации: e-mail не заполнен.")
    private String email;

    @NotBlank(message = "Ошибка валидации: имя не заполнено.")
    private String name;

    public UserDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserDTO(Long id) {
        this.id = id;
    }
}
