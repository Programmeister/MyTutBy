package by.pavel.mytutby.ui.feed;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import by.pavel.mytutby.data.Feed;
import by.pavel.mytutby.databinding.HolderFeedBinding;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedHolder> {

    private final List<Feed> feeds = new ArrayList<>();
    private final FeedViewModel viewModel;

    public FeedAdapter(FeedViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HolderFeedBinding binding = HolderFeedBinding.inflate(inflater, parent, false);
        return new FeedHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedHolder holder, int position) {
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

    class FeedHolder extends RecyclerView.ViewHolder {

        private final HolderFeedBinding binding;

        public FeedHolder(@NonNull HolderFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.deleteButton.setOnClickListener(v ->
                    viewModel.deleteFeed(feeds.get(getAdapterPosition()))
            );
        }

        public void bind(@NonNull Feed feed) {
            binding.title.setText(feed.title);
        }
    }
}
