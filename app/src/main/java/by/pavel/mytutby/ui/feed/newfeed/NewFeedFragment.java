package by.pavel.mytutby.ui.feed.newfeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.pavel.mytutby.R;
import by.pavel.mytutby.databinding.FragmentNewFeedBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewFeedFragment extends Fragment {

    private NewFeedViewModel viewModel;
    private FragmentNewFeedBinding binding;
    private NewFeedAdapter newFeedAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentNewFeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewFeedViewModel.class);
        newFeedAdapter = new NewFeedAdapter(viewModel);
        binding.newFeedsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newFeedsList.setAdapter(newFeedAdapter);
        viewModel.getData().observe(getViewLifecycleOwner(), feeds -> {
            if (feeds != null)
                newFeedAdapter.setFeeds(feeds);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_feeds, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_feed).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.loadFeeds(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }
}
