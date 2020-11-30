package by.pavel.mytutby.repository.item;

import android.content.Context;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.db.ItemDao;
import dagger.hilt.android.qualifiers.ApplicationContext;

public class DetailsRepository implements ItemRepository {

    private final ItemDao dao;
    private final ExecutorService executorService;
    private final Context appContext;

    @Inject
    public DetailsRepository(ItemDao itemDao, @ApplicationContext Context context) {
        dao = itemDao;
        appContext = context;
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public Item getItem(String link) {
        Future<Item> future = executorService.submit(() -> dao.getItem(link));
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public void updateItem(Item item) {
        item.state = appContext.getString(R.string.reading);
        executorService.submit(() ->
                dao.updateItem(item)
        );
    }

    @Override
    public void deleteItem(Item item) {
        executorService.submit(() ->
                dao.deleteItem(item)
        );
    }
}
