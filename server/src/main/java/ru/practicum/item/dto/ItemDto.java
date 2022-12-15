package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.model.LastAndNextDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.dto.UserDTO;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDTO owner;
    private List<Booking> bookings;
    private LastAndNextDto nextBooking;
    private LastAndNextDto lastBooking;

    private List<CommentDto> comments;
    private long requestId;

    public ItemDto(Long id, String name, String description, Boolean available, UserDTO owner, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
    }

    public ItemDto toItemDto(Item item) {
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



    public ItemDto toItemDto(Item item, Optional<Booking> nextBooking,
                             Optional<Booking> lastBooking, List<CommentDto> comments) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = new UserDTO().toUserDTO(item.getOwner());
        this.comments = comments;
        if (nextBooking.isPresent()) {
            this.nextBooking = new LastAndNextDto().getLastAndNextDto(nextBooking.get());
        }
        if (lastBooking.isPresent()) {
            this.lastBooking = new LastAndNextDto().getLastAndNextDto(lastBooking.get());
        }
        return this;
    }

    public ItemDto toItemDto(Item item, List<CommentDto> comments) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = new UserDTO().toUserDTO(item.getOwner());
        this.comments = comments;
        return this;
    }


}