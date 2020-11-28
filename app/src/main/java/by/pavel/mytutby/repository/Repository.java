package by.pavel.mytutby.repository;

import java.util.List;

import androidx.lifecycle.LiveData;
import by.pavel.mytutby.data.Item;

public interface Repository {

    LiveData<List<Item>> getItems();

    void loadItems();
}
