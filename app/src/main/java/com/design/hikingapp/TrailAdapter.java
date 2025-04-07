package com.design.hikingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.TrailViewHolder> {

    private List<Trail> trailList;

    public TrailAdapter(List<Trail> trailList) {
        this.trailList = trailList;
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trail_item_layout, parent, false);
        return new TrailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        // Get the trail object for the current position
        Trail trail = trailList.get(position);

        // Set the trail name, details, and image to the corresponding views in the holder
        holder.trailName.setText(trail.getTrailName());
        holder.trailDetails.setText(trail.getTrailDetails());
        holder.trailImage.setImageResource(trail.getImageResource());

        // Add click listener to the item view to pass data to the new page (Fragment or Activity)
        holder.itemView.setOnClickListener(v -> {
            // Create a bundle to pass the trail details
            Bundle bundle = new Bundle();
            bundle.putString("trail_name", trail.getTrailName());
            bundle.putString("trail_details", trail.getTrailDetails());
            bundle.putInt("trail_image", trail.getImageResource());  // Pass image resource ID

            // Open the TrailDetailFragment and pass the bundle with the data
            TrailDetailsFragment detailFragment = new TrailDetailsFragment();
            detailFragment.setArguments(bundle); // Pass data to the fragment

            // Assuming you're in an Activity context, you can load the fragment:
            FragmentActivity activity = (FragmentActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fragment_pop_in,  // Entering fragment animation
                            R.anim.fragment_pop_out, // Exiting fragment animation
                            R.anim.fragment_pop_in,  // Pop-back enter animation
                            R.anim.fragment_pop_out  // Pop-back exit animation
                    )
                    .replace(R.id.fragment_container, detailFragment) // Replace with the container ID of your fragment
                    .addToBackStack(null) // Add to the back stack to allow back navigation
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return trailList.size();
    }

    public static class TrailViewHolder extends RecyclerView.ViewHolder {
        ImageView trailImage;
        TextView trailName;
        TextView trailDetails;

        public TrailViewHolder(@NonNull View itemView) {
            super(itemView);
            trailImage = itemView.findViewById(R.id.trail_image);
            trailName = itemView.findViewById(R.id.trail_name);
            trailDetails = itemView.findViewById(R.id.trail_details);
        }
    }
}