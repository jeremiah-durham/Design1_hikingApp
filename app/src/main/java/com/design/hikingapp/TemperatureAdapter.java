package com.design.hikingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder> {

    private List<TemperatureHour> weatherList;

    public TemperatureAdapter(List<TemperatureHour> weatherList) {
        this.weatherList = weatherList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureText, timeText;
        ImageView weatherIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            temperatureText = itemView.findViewById(R.id.temperatureText);
            timeText = itemView.findViewById(R.id.timeText);
            weatherIcon = itemView.findViewById(R.id.weatherImage);
        }
    }

    @NonNull
    @Override
    public TemperatureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemperatureAdapter.ViewHolder holder, int position) {
        TemperatureHour item = weatherList.get(position);
        holder.temperatureText.setText(item.temperature);
        holder.weatherIcon.setImageResource(item.iconResId);
        holder.timeText.setText(item.time);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}