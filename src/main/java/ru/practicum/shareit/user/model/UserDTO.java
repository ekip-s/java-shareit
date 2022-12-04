package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;

    public UserDTO toUserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        return this;
    }
}
