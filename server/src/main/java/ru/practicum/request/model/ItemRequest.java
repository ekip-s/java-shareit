package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "creation_date")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime creationDate;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "requestId")
    List<Item> items;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User requestAuthor;

    public ItemRequest(Long id) {
        this.id = id;
    }

    public ItemRequest(String description) {
        this.description = description;
    }
}
