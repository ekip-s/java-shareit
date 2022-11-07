package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name")
    @NotBlank(message = "Ошибка валидации: не заполнено название.")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Ошибка валидации: не заполнено описание.")
    private String description;

    @Column(name = "available")
    @NotNull(message = "Ошибка валидации: не указан available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Transient
    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;

    @Transient
    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Item getItemOfDto(ItemDto itemDto) {
        this.id = itemDto.getId();
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.available = itemDto.getAvailable();
        this.owner = itemDto.getOwner();
        return this;
    }
}