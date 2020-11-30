package by.pavel.mytutby.repository.list;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Response;

public class ItemsRepository implements by.pavel.mytutby.repository.list.ListRepository {

    private final ItemDao dao;
    private final RssService api;
    private final ExecutorService executorService;
    private final ToastProvider toastProvider;
    private final Context appContext;

    @Inject
    public ItemsRepository(
            ItemDao itemDao,
            RssService rssService,
            ToastProvider toaster,
            @ApplicationContext Context context
    ) {
        dao = itemDao;
        api = rssService;
        toastProvider = toaster;
        executorService = Executors.newFixedThreadPool(1);
        appContext = context;
    }

    @Override
    public LiveData<List<Item>> getItems() {
        return dao.getItems();
    }

    @Override
    public void loadItems() {
        executorService.submit(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        appContext.openFileInput(appContext.getString(R.string.file_name))
                ));
                List<String> feeds = new ArrayList<>();
                String line;
                String separator = appContext.getString(R.string.separator);
                while ((line = reader.readLine()) != null)
                    feeds.add(line.split((separator))[1]);
                reader.close();
                dao.clearItems();
                for (String feed: feeds) {
                    Response<Rss> response = api.getRss(feed).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        List<Post> list = response.body().items;
                        List<Item> items = new ArrayList<>();
                        String newState = appContext.getString(R.string.new_);
                        for (Post item : list)
                            items.add(new Item(item.title, item.link, item.description, item.pubDate, newState));
                        dao.insertItems(items);
                    } else toastProvider.showToast(R.string.source_problem);
                }
            } catch (IOException e) {
                toastProvider.showToast(R.string.connection_problem);
            }
        });
    }
}
