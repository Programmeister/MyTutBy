package by.pavel.mytutby.ui.list;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.repository.Repository;

public class ListViewModel extends ViewModel {

    private final Repository repository;

    final LiveData<List<Item>> items;

    @ViewModelInject
    public ListViewModel(
            @NonNull Repository repository
    ) {
        this.repository = repository;
        items = repository.getItems();
    }

    public void update() {
        repository.loadItems();
    }
}