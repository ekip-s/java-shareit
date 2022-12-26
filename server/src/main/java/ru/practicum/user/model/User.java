package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.request.model.ItemRequest;
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

    @Column(name = "email")
    @Email(message = "Ошибка валидации: e-mail введен неправильно.")
    @NotNull(message = "Ошибка валидации: e-mail не заполнен.")
    private String email;


    @Column(name = "user_name")
    @NotBlank(message = "Ошибка валидации: имя не заполнено.")
    private String name;

    @Transient
    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @Transient
    @OneToMany(mappedBy = "booker")
    private  List<Booking> bookings;

    @Transient
    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @Transient
    @OneToMany(mappedBy = "requestAuthor")
    private List<ItemRequest> requests;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(Long id) {
        this.id = id;
    }
}
