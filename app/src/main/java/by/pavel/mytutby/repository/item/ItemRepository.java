package by.pavel.mytutby.repository.item;

import by.pavel.mytutby.data.Item;

public interface ItemRepository {

    Item getItem(String link);

    void updateItem(Item item);

    void deleteItem(Item item);
}
