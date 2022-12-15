package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.item.dto.ItemDTORequest;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.dto.UserDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime created;
    private List<ItemDTORequest> items;
    private UserDTO requestAuthor;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        this.id = itemRequest.getId();
        this.description = itemRequest.getDescription();
        this.created = itemRequest.getCreationDate();
        this.items = toItemDTORequestList(itemRequest.getItems());
        this.requestAuthor = new UserDTO().toUserDTO(itemRequest.getRequestAuthor());
        return this;
    }

    private List<ItemDTORequest> toItemDTORequestList(List<Item> itemsList) {
        if (itemsList == null) {
            return new ArrayList<>();
        } else if (itemsList.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemsList.stream()
                    .map(i -> new ItemDTORequest().toItemDTORequest(i))
                    .collect(Collectors.toList());
        }
    }

}