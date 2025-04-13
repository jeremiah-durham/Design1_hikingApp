package com.design.hikingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WindAdapter extends RecyclerView.Adapter<WindAdapter.WindViewHolder> {

    private final List<WindHour> windList;

    public WindAdapter(List<WindHour> windList) {
        this.windList = windList;
    }

    public static class WindViewHolder extends RecyclerView.ViewHolder {
        TextView speedText, timeText;
        ImageView directionArrow;

        public WindViewHolder(View itemView) {
            super(itemView);
            speedText = itemView.findViewById(R.id.windSpeedText);
            timeText = itemView.findViewById(R.id.windTimeText);
            directionArrow = itemView.findViewById(R.id.windArrow);
        }
    }

    @Override
    public WindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wind_item_layout, parent, false);
        return new WindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WindViewHolder holder, int position) {
        WindHour data = windList.get(position);
        holder.speedText.setText(data.speed + " mph");
        holder.timeText.setText(data.time);
        System.out.println(data.direction);
        holder.directionArrow.setRotation(data.direction); // direction in degrees
    }

    @Override
    public int getItemCount() {
        return windList.size();
    }
}
