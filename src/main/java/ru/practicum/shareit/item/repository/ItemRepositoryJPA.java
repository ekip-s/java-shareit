package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepositoryJPA extends JpaRepository<Item, Long> {

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String searchQuery, String searchQuery2);

    List<Item> findByOwnerOrderById(User owner);
}
