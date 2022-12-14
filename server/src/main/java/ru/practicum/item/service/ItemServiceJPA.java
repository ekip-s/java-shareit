package ru.practicum.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.booking.repository.BookingRepositoryJPA;
import ru.practicum.booking.service.BookingServiceJPA;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.CommentRepositoryJPA;
import ru.practicum.item.repository.ItemRepositoryJPA;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserServiceJPA;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@Transactional(readOnly = true)
public class ItemServiceJPA implements ItemService {

    private final ItemRepositoryJPA itemRepositoryJPA;
    private final UserServiceJPA userServiceJPA;
    private final BookingServiceJPA bookingService;
    private final BookingRepositoryJPA bookingRepositoryJPA;
    private final CommentRepositoryJPA commentRepositoryJPA;

    @Autowired
    public ItemServiceJPA(ItemRepositoryJPA itemRepositoryJPA,
                          UserServiceJPA userServiceJPA,
                          BookingServiceJPA bookingService,
                          BookingRepositoryJPA bookingRepositoryJPA,
                          CommentRepositoryJPA commentRepositoryJPA) {
        this.itemRepositoryJPA = itemRepositoryJPA;
        this.userServiceJPA = userServiceJPA;
        this.bookingService = bookingService;
        this.bookingRepositoryJPA = bookingRepositoryJPA;
        this.commentRepositoryJPA = commentRepositoryJPA;
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        return itemDtoList(itemRepositoryJPA.findByOwnerOrderById(new User(userId)), userId);
    }

    @Override
    public List<ItemDto> getItems(long userId, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id").descending());
        return itemDtoListPage(itemRepositoryJPA.findByOwner(new User(userId), page), userId);
    }



    @Override
    public ItemDto getById(long userId, long itemId) {
        return toItemDto(checkEntity(itemRepositoryJPA.findById(itemId)), userId);
    }

    @Transactional
    @Override
    public ItemDto addNewItem(long userId, Item item) {
        userServiceJPA.getById(userId);
        item.setOwner(new User(userId));
        return new ItemDto().toItemDto(itemRepositoryJPA.save(item));
    }

    @Transactional
    @Override
    public Item updateItem(long userId, Item item, long itemId) {
        Item initialItem = checkEntity(itemRepositoryJPA.findById(itemId));
        checkOwner(userId, initialItem.getOwner().getId());
        if (item.getName() != null) {
            initialItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            initialItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            initialItem.setAvailable(item.getAvailable());
        }
        return itemRepositoryJPA.save(initialItem);
    }

    @Transactional
    @Override
    public void deleteItem(long userId, long itemId) {
        itemRepositoryJPA.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemDtoList(itemRepositoryJPA.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text,
                    text), userId);
        }
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by("id").descending());
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemDtoListPage(itemRepositoryJPA.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(
                    text, text, page), userId);
        }
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, Comment comment) {
        User user = userServiceJPA.getById(userId);
        Item item = checkEntity(itemRepositoryJPA.findById(itemId));
        verificationCompletedBooking(user, item);
        comment.setItemAndAuthor(item, user);
        commentRepositoryJPA.save(comment);
        return new CommentDto().toCommentDto(comment);
    }

    private Item checkEntity(Optional<Item> item) {
        if (item.isEmpty()) {
            throw new IllegalArgumentException("?????? ???????????? ?? ?????????? id.");
        } else {
            return item.get();
        }
    }


    private void checkOwner(long id, long itemUserId) {
        if (id != itemUserId) {
            throw new IllegalArgumentException("?????????? ?????????????????????? ?????????????? ???????????????????????? - ???????????? ????????????????");
        }
    }

    private ItemDto toItemDto(Item item, long userId) {
        if (item.getOwner().getId() == userId) {
            return new ItemDto().toItemDto(item, bookingService.nextBooking(item),
                    bookingService.lastBooking(item), toCommentDtoList(item));
        } else {
            return new ItemDto().toItemDto(item, toCommentDtoList(item));
        }
    }

    private List<ItemDto> itemDtoList(List<Item> items, long userId) {
        return items.stream()
                .map(i -> toItemDto(i, userId))
                .collect(Collectors.toList());
    }

    private List<ItemDto> itemDtoListPage(Page<Item> items, long userId) {
        return items.stream()
                .map(i -> toItemDto(i, userId))
                .collect(Collectors.toList());
    }

    private void verificationCompletedBooking(User user, Item item) {
        if (bookingRepositoryJPA.findByBookerAndItemAndEndBefore(user,
                item, LocalDateTime. now()).isEmpty()) {
            throw new ConflictException("?????????? ?????????? ???????????????? ???????????? ???? ??????????, ?????????????? ???????????????????????? ??????????.");
        }
    }

    private List<CommentDto> toCommentDtoList(Item item) {
        return commentRepositoryJPA.findByItemOrderByCreated(item)
                .stream()
                .map(c -> new CommentDto().toCommentDto(c))
                .collect(Collectors.toList());
    }
}