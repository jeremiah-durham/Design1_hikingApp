package com.design.hikingapp;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrecipAdapter extends RecyclerView.Adapter<PrecipAdapter.ViewHolder> {

    private List<PrecipHour> precipList;
    private LinearLayout barContainer;

    public PrecipAdapter(List<PrecipHour> precipList) {
        this.precipList = precipList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView percentText, timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            percentText = itemView.findViewById(R.id.percentageText);
            timeText = itemView.findViewById(R.id.timeTextPrecip);
            barContainer = itemView.findViewById(R.id.barHeight);
        }
    }

    @NonNull
    @Override
    public PrecipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.precip_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrecipAdapter.ViewHolder holder, int position) {
        int percentage = precipList.get(position).precipPercent; // 0–100

        // Convert max margin from dp to px
        float maxMarginDp = 44f;
        float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        int maxMarginPx = (int) (maxMarginDp * density);

        // Calculate top margin: higher percent = smaller margin
        int marginTopPx = (int) ((1f - (percentage / 100f)) * maxMarginPx);

        // Apply to layout
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) barContainer.getLayoutParams();
        params.topMargin = marginTopPx;
        barContainer.setLayoutParams(params);

        // Update the percentage label
        holder.percentText.setText(percentage + "%");
        holder.timeText.setText(precipList.get(position).time);

    }

    @Override
    public int getItemCount() {
        return precipList.size();
    }
}
