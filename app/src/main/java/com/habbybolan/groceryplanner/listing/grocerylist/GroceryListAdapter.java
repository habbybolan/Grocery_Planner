package com.habbybolan.groceryplanner.listing.grocerylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.habbybolan.groceryplanner.models.Grocery;
import com.habbybolan.groceryplanner.R;
import com.habbybolan.groceryplanner.databinding.GroceryListDetailsBinding;

import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {


    private List<Grocery> groceries;
    private GroceryListView view;

    GroceryListAdapter(List<Grocery> groceries, GroceryListView view) {
        this.groceries = groceries;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GroceryListDetailsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.grocery_list_details, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grocery grocery = groceries.get(position);
        holder.bind(grocery);
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final GroceryListDetailsBinding binding;

        ViewHolder(GroceryListDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.groceryListContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onGrocerySelected(groceries.get(getAdapterPosition()));
                }
            });
        }

        void bind(Grocery grocery) {
            binding.setListName(grocery.getName());
        }
    }
}
