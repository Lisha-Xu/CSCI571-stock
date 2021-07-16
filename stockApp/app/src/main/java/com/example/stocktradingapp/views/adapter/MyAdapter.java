package com.example.stocktradingapp.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktradingapp.R;
import com.example.stocktradingapp.entity.Stock;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Stock> mStockList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_stock_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.stockNameItemTv.setText(mStockList.get(position).getStockName());
//        holder.stockValueItemTv.setText(mStockList.get(position).getStockValue());
//        holder.stockShareItemTv.setText(mStockList.get(position).getStockShares());
//        holder.stockTrendItemTv.setText(mStockList.get(position).getStockTrend());
    }

    @Override
    public int getItemCount() {
        return mStockList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        private final TextView stockNameItemTv;
//        private final TextView stockValueItemTv;
//        private final TextView stockShareItemTv;
//        private final TextView stockTrendItemTv;
//        private final ImageView stockTrendItemImageView;

        public ViewHolder(View itemView) {
            super(itemView);
//            stockNameItemTv = (TextView) itemView.findViewById(R.id.stock_name_tv);
//            stockValueItemTv = (TextView) itemView.findViewById(R.id.stock_value_tv);
//            stockShareItemTv = (TextView) itemView.findViewById(R.id.stock_shares_tv);
//            stockTrendItemTv = (TextView) itemView.findViewById(R.id.stock_trend_tv);
//            stockTrendItemImageView = (ImageView) itemView.findViewById(R.id.stock_trend_imv);
        }
    }
}
