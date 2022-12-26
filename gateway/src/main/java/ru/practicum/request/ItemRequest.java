package ru.practicum.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    private Long id;

    @NotEmpty
    private String description;

    public ItemRequest(Long id) {
        this.id = id;
    }

    public ItemRequest(String description) {
        this.description = description;
    }
}
