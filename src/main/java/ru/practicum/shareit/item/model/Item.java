package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    @Min(value = 1, message = "Ошибка валидации: не указан id пользователя.")
    private Long userId;
    @NotBlank(message = "Ошибка валидации: не заполнено название.")
    private String name;
    @NotBlank(message = "Ошибка валидации: не заполнено описание.")
    private String description;
    @NotNull(message = "Ошибка валидации: не указан available")
    private Boolean available;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
}