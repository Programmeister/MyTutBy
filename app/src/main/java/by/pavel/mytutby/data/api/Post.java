package by.pavel.mytutby.data.api;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Post {
    @PropertyElement
    public String title;
    @PropertyElement
    public String link;
    @PropertyElement
    public String description;
    @PropertyElement
    public String pubDate;
}
