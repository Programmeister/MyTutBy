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
    private final MutableLiveData<List<Feed>> data;

    @ViewModelInject
    public FeedViewModel(FeedRepository repository) {
        this.repository = repository;
        data = new MutableLiveData<>();
    }

    public LiveData<List<Feed>> getData() {
        return data;
    }

    public void readFeeds() {
        data.setValue(repository.getFeeds());
    }

    public void deleteFeed(Feed feed) {
        repository.deleteFeed(feed.title);
        List<Feed> list = data.getValue();
        if (list != null) {
            list.remove(feed);
            data.setValue(list);
        }
    }
}
