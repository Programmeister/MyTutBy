package by.pavel.mytutby.repository.feed.newfeed;

import java.util.List;

import by.pavel.mytutby.data.Feed;

public interface NewFeedRepository {

    List<Feed> getNewFeeds(String link);

    void addFeed(Feed feed);
}
