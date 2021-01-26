package by.pavel.mytutby.ui.feed.newfeed;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.pavel.mytutby.data.Feed;
import by.pavel.mytutby.databinding.HolderNewFeedBinding;

public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.NewFeedHolder> {

    private final List<Feed> feeds = new ArrayList<>();
    private final NewFeedViewModel viewModel;

    public NewFeedAdapter(NewFeedViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NewFeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HolderNewFeedBinding binding = HolderNewFeedBinding.inflate(inflater, parent, false);
        return new NewFeedHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewFeedHolder holder, int position) {
        holder.bind(feeds.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void setFeeds(List<Feed> newFeeds) {
        feeds.clear();
        feeds.addAll(newFeeds);
        notifyDataSetChanged();
    }

    class NewFeedHolder extends RecyclerView.ViewHolder {

        private final HolderNewFeedBinding binding;

        public NewFeedHolder(@NonNull HolderNewFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.addButton.setOnClickListener(v ->
                    viewModel.addFeed(feeds.get(getAdapterPosition()))
            );
        }

        public void bind(@NonNull Feed feed) {
            binding.title.setText(feed.title);
        }
    }
}
