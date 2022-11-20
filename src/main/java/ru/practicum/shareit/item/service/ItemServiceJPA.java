package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.BookingRepositoryJPA;
import ru.practicum.shareit.booking.service.BookingServiceJPA;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepositoryJPA;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
@Validated
@Transactional(readOnly = true)
public class ItemServiceJPA implements ItemService {

    ItemRepositoryJPA itemRepositoryJPA;
    UserServiceJPA userServiceJPA;
    BookingServiceJPA bookingService;
    BookingRepositoryJPA bookingRepositoryJPA;
    CommentRepositoryJPA commentRepositoryJPA;

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
    public ItemDto getById(long userId, long itemId) {
        Optional<Item> item = itemRepositoryJPA.findById(itemId);
        checkEntity(item, itemId);
        return toItemDto(item.get(), userId);
    }

    @Transactional
    @Override
    public Item addNewItem(long userId, @Valid Item item) {
        userServiceJPA.getById(userId);
        item.setOwner(new User(userId));
        return itemRepositoryJPA.save(item);
    }

    @Transactional
    @Override
    public Item updateItem(long userId, Item item, long itemId) {
        Item initialItem = new Item().getItemOfDto(getById(userId, itemId));
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
    public List<Item> searchItem(long userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return itemRepositoryJPA.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        }
    }

    @Transactional
    @Override
    public CommentDto addComment(long userId, long itemId, Comment comment) {
        User user = userServiceJPA.getById(userId);
        Optional<Item> itemOptional = itemRepositoryJPA.findById(itemId);
        checkEntity(itemOptional, itemId);
        Item item = itemOptional.get();
        verificationCompletedBooking(user, item);
        comment.setItemAndAuthor(item, user);
        commentRepositoryJPA.save(comment);
        return new CommentDto().toCommentDto(comment);
    }

    private void checkEntity(Optional<Item> item, Long itemId) {
        if (item.isEmpty())
            throw new IllegalArgumentException("Нет товара с id =" + itemId);
    }

    private void checkOwner(long id, long itemUserId) {
        if (id != itemUserId) {
            throw new IllegalArgumentException("Товар принадлежит другому пользователю - нельзя обновить");
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

    private void verificationCompletedBooking(User user, Item item) {
        if (bookingRepositoryJPA.findByBookerAndItemAndEndBefore(user,
                item, LocalDateTime. now()).isEmpty()) {
            throw new ConflictException("Отзыв можно оставить только на товар, который бронировался ранее.");
        }
    }

    private List<CommentDto> toCommentDtoList(Item item) {
        return commentRepositoryJPA.findByItemOrderByCreated(item)
                .stream()
                .map(c -> new CommentDto().toCommentDto(c))
                .collect(Collectors.toList());
    }
}
