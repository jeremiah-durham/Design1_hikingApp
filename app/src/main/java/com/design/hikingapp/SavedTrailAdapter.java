package com.design.hikingapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.design.hikingapp.trail.Trail;
import com.design.hikingapp.weather.WeatherRepository;

import java.util.List;

public class SavedTrailAdapter extends RecyclerView.Adapter<SavedTrailAdapter.TrailViewHolder> {

    private List<Trail> trailList;
//    private boolean buttonState = false;

    public SavedTrailAdapter(List<Trail> trailList) {
        this.trailList = trailList;
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_trail_item_layout, parent, false);
        return new TrailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        // Get the trail object for the current position
        Trail trail = trailList.get(position);

        // Set the trail name, details, and image to the corresponding views in the holder
        holder.trailName.setText(trail.getName());
        Bitmap bmp = null;
        if((bmp = trail.getImgBmp()) == null) {
            holder.trailImage.setImageResource(trail.getImageResource());
        } else {
            holder.trailImage.setImageBitmap(bmp);
        }

        // Add click listener to the item view to pass data to the new page (Fragment or Activity)
        holder.itemView.setOnClickListener(v -> {
            /**
             * Load in the saved fragment, idk how you want to do this
             */
            Bundle bundle = new Bundle();
            bundle.putString("trail_name", trail.getName());
            bundle.putInt("trail_image", trail.getImageResource());  // Pass image resource ID

            // Open the TrailDetailFragment and pass the bundle with the data
            SavedTrailDetailsFragment detailFragment = new SavedTrailDetailsFragment(trail, WeatherRepository.getInstance());

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

//        // Add click listener to the startButton to start a hike
//        holder.startButton.setOnClickListener(v -> {
//            if (buttonState == false) {
//                holder.startButton.setImageResource(R.drawable.stop_button);
//            } else {
//                holder.startButton.setImageResource(R.drawable.start_button);
//            }
//            buttonState = !buttonState;
//        });
    }

    @Override
    public int getItemCount() {
        return trailList.size();
    }

    public static class TrailViewHolder extends RecyclerView.ViewHolder {
        ImageView trailImage;
        TextView trailName;
        ImageButton startButton;

        public TrailViewHolder(@NonNull View itemView) {
            super(itemView);
            trailImage = itemView.findViewById(R.id.trail_image);
            trailName = itemView.findViewById(R.id.trail_name);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }
}