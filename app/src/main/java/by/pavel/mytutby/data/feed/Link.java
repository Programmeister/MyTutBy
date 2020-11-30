package by.pavel.mytutby.data.feed;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Link {
    @Attribute
    public String type;
    @Attribute
    public String title;
    @Attribute
    public String href;
}
