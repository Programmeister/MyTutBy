package by.pavel.mytutby.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @NonNull
    @PrimaryKey
    public final String link;
    public final String title;
    public final String description;
    public final String pubDate;
    public String state;

    public Item(String title, @NonNull String link, String description, String pubDate, String state) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.state = state;
    }
}
