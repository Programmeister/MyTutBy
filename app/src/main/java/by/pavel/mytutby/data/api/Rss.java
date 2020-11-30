package by.pavel.mytutby.data.api;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Path;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml//(writeNamespaces = {
//        "xmlns:atom=http://www.w3.org/2005/Atom",
//        "xmlns:media=http://search.yahoo.com/mrss/"
//})
public class Rss {
    @Path("channel")
    @Element(name = "item")
    public List<Post> items;
}
