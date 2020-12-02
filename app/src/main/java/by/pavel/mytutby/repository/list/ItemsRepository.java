package by.pavel.mytutby.repository.list;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.data.api.Post;
import by.pavel.mytutby.data.api.Rss;
import by.pavel.mytutby.db.ItemDao;
import by.pavel.mytutby.network.RssService;
import by.pavel.mytutby.util.dateformatter.DateFormatter;
import by.pavel.mytutby.util.toaster.ToastProvider;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Response;

public class ItemsRepository implements ListRepository {

    private final ItemDao dao;
    private final RssService api;
    private final ExecutorService executorService;
    private final ToastProvider toastProvider;
    private final Context appContext;
    private final DateFormatter formatter;

    @Inject
    public ItemsRepository(
            ItemDao itemDao,
            RssService rssService,
            ToastProvider toaster,
            @ApplicationContext Context context,
            DateFormatter dateFormatter
    ) {
        dao = itemDao;
        api = rssService;
        toastProvider = toaster;
        executorService = Executors.newFixedThreadPool(2);
        appContext = context;
        formatter = dateFormatter;
    }

    @Override
    public LiveData<List<Item>> getItems() {
        return dao.getItems();
    }

    @Override
    public void loadItems() {
        executorService.submit(() -> {
            List<Post> posts = getPostFromNetwork(getFeedsFromFile());
            List<Item> items = new ArrayList<>();
            String newState = appContext.getString(R.string.new_);

            long currentTime = Calendar.getInstance().getTimeInMillis();
            long storageTime = getStorageTimeInMillis();

            for (Post post : posts) {
                String stringDate = post.pubDate.substring(5, 25);
                long itemTime = formatter.getTimeInMillis(stringDate);

                if (currentTime - itemTime > storageTime) {
                    items.add(new Item(post.title, post.link,
                            post.description, post.pubDate,
                            itemTime, newState
                    ));
                }
            }
            if (items.isEmpty()) {
                toastProvider.showToast(R.string.no_new_items);
            } else {
                dao.clearItems();
                dao.insertItems(items);
            }
        });
    }

    @Override
    public void deleteOldItem() {
        executorService.submit(() -> {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            long storageTime = getStorageTimeInMillis();
            List<Item> items = dao.getItemsReversed();
            for (Item item : items) {
                if (currentTime - item.time < storageTime)
                    dao.deleteItem(item);
                else return;
            }
        });
    }

    private List<String> getFeedsFromFile() {
        String fileName = appContext.getString(R.string.file_name);
        File file = appContext.getFileStreamPath(fileName);
        if (!file.exists())
            createFeedsFile();
        List<String> feeds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                appContext.openFileInput(fileName)
        ))) {
            String line;
            String separator = appContext.getString(R.string.separator);
            while ((line = reader.readLine()) != null)
                feeds.add(line.split((separator))[1]);
        } catch (IOException e) {
            toastProvider.showToast(R.string.file_not_read);
            e.printStackTrace();
        }
        return feeds;
    }

    private List<Post> getPostFromNetwork(List<String> feeds) {
        List<Post> list = new ArrayList<>();
        for (String feed : feeds) {
            try {
                Response<Rss> response = api.getRss(feed).execute();
                if (response.isSuccessful() && response.body() != null)
                    list.addAll(response.body().items);
                else
                    toastProvider.showToast(R.string.feed_unavailable);
            } catch (IOException e) {
                e.printStackTrace();
                toastProvider.showToast(R.string.connection_problem);
            }
        }
        return list;
    }

    private long getStorageTimeInMillis() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(appContext);
        String storageTimeKey = appContext.getString(R.string.storage_time_key);
        String daysCountString = preferences.getString(storageTimeKey, "7");
        return Long.parseLong(daysCountString) * 24 * 60 * 60;
    }

    private void createFeedsFile() {
        String defaultFeed = appContext.getString(R.string.default_feed);
        String fileName = appContext.getString(R.string.file_name);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                appContext.openFileOutput(fileName, Context.MODE_APPEND)
        ))) {
            writer.append(defaultFeed);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
