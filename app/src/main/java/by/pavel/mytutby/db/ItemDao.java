package by.pavel.mytutby.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import by.pavel.mytutby.data.Item;

@Dao
public interface ItemDao {

    @Query("DELETE FROM items")
    void clearItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(List<Item> items);

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getItems();

    @Query("SELECT * FROM items WHERE link = :link")
    Item getItem(String link);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);
}
