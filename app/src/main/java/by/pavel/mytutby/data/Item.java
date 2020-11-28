package by.pavel.mytutby.data;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Xml
@Entity(tableName = "items")
public class Item {
    @PropertyElement
    public String title;
    @PropertyElement
    @PrimaryKey
    @NonNull
    public String link;
    @PropertyElement
    public String description;
    @PropertyElement
    public String pubDate;
}
