package by.pavel.mytutby.repository.feed;

import java.util.List;

import by.pavel.mytutby.data.Feed;

public interface FeedRepository {

    List<Feed> getFeeds();

    void deleteFeed(String title);
}
