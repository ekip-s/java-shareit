package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDto {
/*здесь доступность не вижу смыла показывать,
 так как будем только доступные выводить.
 Пока не использую, так как тесты проверяют id,
 а его вроде как тоже не нужно отражать;
 */
    public static Item toItemDto(Item item) {
        return new Item(
                item.getName(),
                item.getDescription());
    }
}
