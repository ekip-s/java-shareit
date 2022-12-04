package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class ItemRequestService {

    private final UserRepositoryJPA userRepositoryJPA;
    private final RequestRepositoryJPA requestRepositoryJPA;

    @Autowired
    public ItemRequestService(UserRepositoryJPA userRepositoryJPA,
                              RequestRepositoryJPA requestRepositoryJPA) {
        this.userRepositoryJPA = userRepositoryJPA;
        this.requestRepositoryJPA = requestRepositoryJPA;

    }

    @Transactional
    public ItemRequestDto addNewRequest(ItemRequest itemRequest, long userId) {
        User user = getUser(userId);
        itemRequest.setRequestAuthor(user);
        itemRequest.setCreationDate(LocalDateTime. now());
        return new ItemRequestDto().toItemRequestDto(requestRepositoryJPA.save(itemRequest));
    }

    public List<ItemRequestDto> myRequest (long userId) {
        User user = getUser(userId);
        return toListDTO(requestRepositoryJPA.findByRequestAuthorOrderByCreationDate(user));
    }

    public List<ItemRequestDto> strangerRequest(long userId, int from, int size) {
        User user = getUser(userId);
        checkPaginationParams(from, size);
        from = from / size;
        Pageable page = PageRequest.of(from, size, Sort.by("creationDate").descending());
        return toPageDTO(requestRepositoryJPA.findByRequestAuthorNot(user, page));
    }

    public List<ItemRequestDto> strangerRequest(long userId) {
        User user = getUser(userId);
        return toListDTO(requestRepositoryJPA.findAll());
    }

    public ItemRequestDto requestById(long userId, long requestId) {
        User user = getUser(userId);
        return new ItemRequestDto().toItemRequestDto(getItemRequest(requestId));
    }

    private void checkPaginationParams(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Параметр from не может быть отрицательным.");
        } else if (size < 1) {
            throw new IllegalArgumentException("Параметр size должен быть больше нуля.");
        }
    }


    private User getUser(long userId) {
        Optional<User> optionalUser =  userRepositoryJPA.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ConflictException("Нет пользователя с id = " + userId + ".");
        } else {
            return optionalUser.get();
        }
    }

    private ItemRequest getItemRequest(long requestId) {
        Optional<ItemRequest> optionalItemRequest = requestRepositoryJPA.findById(requestId);
        if (optionalItemRequest.isEmpty()) {
            throw new ConflictException("Нет запроса с id = " + requestId + ".");
        } else {
            return optionalItemRequest.get();
        }
    }

    private List<ItemRequestDto> toListDTO(List<ItemRequest> items) {
        return items.stream()
                .map(i -> new ItemRequestDto().toItemRequestDto(i))
                .collect(Collectors.toList());
    }

    private List<ItemRequestDto> toPageDTO(Page<ItemRequest> items) {
        return items.stream()
                .map(i -> new ItemRequestDto().toItemRequestDto(i))
                .collect(Collectors.toList());
    }
}
