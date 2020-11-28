package by.pavel.mytutby.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import by.pavel.mytutby.data.Item;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(List<Item> items);

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getItems();

    @Query("DELETE FROM items")
    void clearItems();
}
