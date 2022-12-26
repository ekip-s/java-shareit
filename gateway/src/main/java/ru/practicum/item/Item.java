package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;

    @NotBlank(message = "Ошибка валидации: не заполнено название.")
    private String name;

    @NotBlank(message = "Ошибка валидации: не заполнено описание.")
    private String description;

    @NotNull(message = "Ошибка валидации: не указан available")
    private Boolean available;

    private Long requestId;


    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Item(String name, String description, Long requestId) {
        this.name = name;
        this.description = description;
        this.requestId = requestId;
    }

    public Item(Long id) {
        this.id = id;
    }
}