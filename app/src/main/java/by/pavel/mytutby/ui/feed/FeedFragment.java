package by.pavel.mytutby.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import by.pavel.mytutby.R;
import by.pavel.mytutby.databinding.FragmentFeedBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FeedFragment extends Fragment {

    private FragmentFeedBinding binding;
    private FeedViewModel viewModel;
    private FeedAdapter feedAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedAdapter = new FeedAdapter(viewModel);
        binding.myFeedsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.myFeedsList.setAdapter(feedAdapter);
        viewModel.readFeeds();
        viewModel.getData().observe(getViewLifecycleOwner(), feeds -> {
            if (feeds != null)
                feedAdapter.setFeeds(feeds);
        });
        binding.addButton.setOnClickListener(v ->
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_open_newFeedFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
