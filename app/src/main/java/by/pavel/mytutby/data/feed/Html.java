package by.pavel.mytutby.data.feed;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class Html {
    @Path("head")
    @Element
    public List<Link> links;
}
