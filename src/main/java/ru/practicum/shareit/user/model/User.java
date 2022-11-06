package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    @Email(message = "Ошибка валидации: e-mail введен неправильно.")
    @NotNull(message = "Ошибка валидации: e-mail не заполнен.")
    private String email;

    @Column(name = "email")
    @NotBlank(message = "Ошибка валидации: имя не заполнено.")
    private String name;

    @Transient
    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @Transient
    @OneToMany(mappedBy = "booker")
    private  List<Booking> bookings;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(Long id) {
        this.id = id;
    }
}