package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.LastAndNextDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
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
    private User owner;
    private List<Booking> bookings;
    private LastAndNextDto nextBooking;
    private LastAndNextDto lastBooking;

    private List<CommentDto> comments;

    public ItemDto toItemDto(Item item, Optional<Booking> nextBooking,
                             Optional<Booking> lastBooking, List<CommentDto> comments) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = item.getOwner();
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
        this.owner = item.getOwner();
        this.comments = comments;
        return this;
    }


}
