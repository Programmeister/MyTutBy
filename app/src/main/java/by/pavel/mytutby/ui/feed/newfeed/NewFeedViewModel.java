package by.pavel.mytutby.ui.feed.newfeed;

import java.util.List;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import by.pavel.mytutby.data.Feed;
import by.pavel.mytutby.repository.feed.newfeed.NewFeedRepository;

public class NewFeedViewModel extends ViewModel {

    private final NewFeedRepository repository;
    private final MutableLiveData<List<Feed>> data;

    @ViewModelInject
    public NewFeedViewModel(NewFeedRepository repository) {
        this.repository = repository;
        data = new MutableLiveData<>();
    }

    public LiveData<List<Feed>> getData() {
        return data;
    }

    public void loadFeeds(String link) {
        data.setValue(repository.getNewFeeds(link));
    }

    public void addFeed(Feed feed) {
        repository.addFeed(feed);
        List<Feed> list = data.getValue();
        if (list != null) {
            list.remove(feed);
            data.setValue(list);
        }
    }
}
