package by.pavel.mytutby.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import by.pavel.mytutby.data.Item;
import by.pavel.mytutby.databinding.HolderItemBinding;

public class ItemsAdapter
        extends RecyclerView.Adapter<ItemsAdapter.ItemHolder>
        implements Filterable {

    private final List<Item> items = new ArrayList<>();
    private List<Item> filteredItems = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HolderItemBinding binding = HolderItemBinding.inflate(inflater, parent, false);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(filteredItems.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    public void setItems(List<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        filteredItems.clear();
        filteredItems.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String string = constraint.toString();
                if (string.isEmpty()) {
                    filteredItems = items;
                } else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item item : items)
                        if (item.title.toLowerCase().contains(string.toLowerCase()))
                            filteredList.add(item);
                    filteredItems = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private final HolderItemBinding binding;

        public ItemHolder(@NonNull HolderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.title.setOnClickListener(v -> {
                Item item = items.get(getAdapterPosition());
                Navigation.findNavController(v)
                        .navigate(ListFragmentDirections.actionOpenDetails(item.link));
            });
            binding.annotationButton.setOnClickListener(v -> {
                if (binding.description.getVisibility() == View.GONE) {
                    binding.description.setVisibility(View.VISIBLE);
                    binding.date.setVisibility(View.VISIBLE);
                } else {
                    binding.description.setVisibility(View.GONE);
                    binding.date.setVisibility(View.GONE);
                }
            });
        }

        public void bind(@NonNull Item item) {
            binding.title.setText(item.title);
            binding.description.setText(item.description);
            binding.date.setText(item.pubDate);
        }
    }
}
