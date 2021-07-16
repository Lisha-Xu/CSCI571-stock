package com.example.stocktradingapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    View rootView;
    TextView titleView;
    TextView expandView;

    public HeaderViewHolder(View view) {
        super(view);
        rootView = view;
        titleView = view.findViewById(R.id.tvTitle);
        expandView = view.findViewById(R.id.net_worth_tv);

    }
}
