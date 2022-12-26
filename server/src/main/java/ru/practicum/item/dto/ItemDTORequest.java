package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.model.Item;
import ru.practicum.user.dto.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTORequest {
    private Long id;
    private String name;
    private String description;
    private UserDTO owner;
    private Boolean available;
    private long requestId;


    public ItemDTORequest toItemDTORequest(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = new UserDTO().toUserDTO(item.getOwner());
        if (item.getRequestId() != null) {
            this.requestId = item.getRequestId().getId();
        }
        return this;
    }
}
