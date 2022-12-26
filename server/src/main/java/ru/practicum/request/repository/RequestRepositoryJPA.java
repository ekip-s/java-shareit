package ru.practicum.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.ItemRequest;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface RequestRepositoryJPA extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestAuthorOrderByCreationDate(User user);

    Page<ItemRequest> findByRequestAuthorNot(User user, Pageable pageable);
}