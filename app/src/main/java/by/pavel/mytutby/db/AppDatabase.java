package by.pavel.mytutby.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import by.pavel.mytutby.data.Item;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();
}
