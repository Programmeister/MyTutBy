package by.pavel.mytutby.ui.item;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.repository.item.ItemRepository;

public class ItemViewModel extends ViewModel {

    private final ItemRepository repository;
    private Item item;

    @ViewModelInject
    public ItemViewModel(ItemRepository repository) {
        this.repository = repository;
    }

    public void loadItem(String link) {
        item = repository.getItem(link);
    }

    public void markReading() {
        repository.updateItem(item);
    }

    public void markDone() {
        repository.deleteItem(item);
    }
}
