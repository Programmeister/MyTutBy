package by.pavel.mytutby.repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.data.Rss;
import by.pavel.mytutby.db.ItemDao;
import by.pavel.mytutby.network.RssService;
import by.pavel.mytutby.util.toaster.ToastProvider;
import retrofit2.Response;

public class ListRepository implements Repository {

    private final ItemDao dao;
    private final RssService service;
    private final ExecutorService executorService;
    private final ToastProvider toastProvider;

    @Inject
    public ListRepository(
            ItemDao itemDao,
            RssService rssService,
            ToastProvider toaster) {
        dao = itemDao;
        service = rssService;
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
                Response<Rss> response = service.getRss().execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body().items;
                    dao.clearItems();
                    dao.insertItems(items);
                } else toastProvider.showToast(R.string.source_problem);
            } catch (IOException e) {
                toastProvider.showToast(R.string.connection_problem);
            }
        });
    }
}
