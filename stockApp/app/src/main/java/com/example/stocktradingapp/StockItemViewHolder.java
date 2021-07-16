package com.example.stocktradingapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StockItemViewHolder extends RecyclerView.ViewHolder {
    final View rootView;
    final TextView stockNameItemTv;
    final TextView stockValueItemTv;
    final TextView stockShareItemTv;
    final TextView stockTrendItemTv;
    final ImageView stockTrendItemImageView;
    final ImageView stockDetailImageView;

    public StockItemViewHolder(View itemView) {
        super(itemView);
        rootView = itemView;
        stockNameItemTv = (TextView) itemView.findViewById(R.id.stock_name_tv);
        stockValueItemTv = (TextView) itemView.findViewById(R.id.stock_value_tv);
        stockShareItemTv = (TextView) itemView.findViewById(R.id.stock_shares_tv);
        stockTrendItemTv = (TextView) itemView.findViewById(R.id.stock_trend_tv);
        stockTrendItemImageView = (ImageView) itemView.findViewById(R.id.stock_trend_imv);
        stockDetailImageView = (ImageView)itemView.findViewById(R.id.stock_arrow_imv);
    }


}
