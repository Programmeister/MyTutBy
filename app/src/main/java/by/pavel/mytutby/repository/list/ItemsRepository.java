package by.pavel.mytutby.repository.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.data.api.Post;
import by.pavel.mytutby.data.api.Rss;
import by.pavel.mytutby.db.ItemDao;
import by.pavel.mytutby.network.RssService;
import by.pavel.mytutby.util.toaster.ToastProvider;
import retrofit2.Response;

public class ItemsRepository implements by.pavel.mytutby.repository.list.ListRepository {

    private final ItemDao dao;
    private final RssService api;
    private final ExecutorService executorService;
    private final ToastProvider toastProvider;

    @Inject
    public ItemsRepository(
            ItemDao itemDao,
            RssService rssService,
            ToastProvider toaster
    ) {
        dao = itemDao;
        api = rssService;
        toastProvider = toaster;
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public LiveData<List<Item>> getItems() {
        return dao.getItems();
    }

    @Override
    public void loadItems() {
        executorService.submit(() -> {
            try {
                Response<Rss> response = api.getRss().execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Post> list = response.body().items;
                    List<Item> items = new ArrayList<>();
                    for (Post item : list)
                        items.add(new Item(item.title, item.link, item.description, item.pubDate, "new"));
                    dao.clearItems();
                    dao.insertItems(items);
                } else toastProvider.showToast(R.string.source_problem);
            } catch (IOException e) {
                toastProvider.showToast(R.string.connection_problem);
            }
        });
    }
}
