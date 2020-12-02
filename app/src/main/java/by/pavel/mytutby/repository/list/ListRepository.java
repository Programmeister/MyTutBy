package by.pavel.mytutby.repository.list;

import java.util.List;

import androidx.lifecycle.LiveData;
import by.pavel.mytutby.data.Item;

public interface ListRepository {

    LiveData<List<Item>> getItems();

    void loadItems();

    void deleteOldItem();
}
