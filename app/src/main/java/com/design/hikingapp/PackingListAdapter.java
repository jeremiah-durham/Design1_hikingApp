package com.design.hikingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PackingListAdapter extends RecyclerView.Adapter<PackingListAdapter.ViewHolder> {

    private final List<PackingItem> itemList;

    public PackingListAdapter(List<PackingItem> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }

    @NonNull
    @Override
    public PackingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.packing_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackingListAdapter.ViewHolder holder, int position) {
        PackingItem item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemImage.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
