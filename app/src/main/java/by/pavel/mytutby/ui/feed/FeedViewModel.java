package by.pavel.mytutby.ui.feed;

import java.util.List;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import by.pavel.mytutby.data.Feed;
import by.pavel.mytutby.repository.feed.FeedRepository;

public class FeedViewModel extends ViewModel {

    private final FeedRepository repository;
    private final MutableLiveData<List<Feed>> feeds;

    @ViewModelInject
    public FeedViewModel(FeedRepository repository) {
        this.repository = repository;
        feeds = new MutableLiveData<>();
        feeds.setValue(repository.getFeeds());
    }

    public LiveData<List<Feed>> getFeeds() {
        return feeds;
    }

    public void deleteFeed(Feed feed) {
        repository.deleteFeed(feed.title);
        feeds.setValue(repository.getFeeds());
    }
}