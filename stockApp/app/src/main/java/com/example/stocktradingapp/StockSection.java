package com.example.stocktradingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktradingapp.common.MyApplication;
import com.example.stocktradingapp.detail.StockDetailActivity;
import com.example.stocktradingapp.entity.Stock;
import com.example.stocktradingapp.views.RecycleItemTouchHelper;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

import static com.example.stocktradingapp.common.Constant.MY_DOLLAR;

public class StockSection extends Section implements RecycleItemTouchHelper.ItemTouchHelperCallback{

    private final ClickListener clickListener;
    String title;
    List<Stock> mStockList;
    Context context;

    /**
     * Create a Section object based on {@link SectionParameters}.
     *
     * @param sectionParameters section parameters
     * @param clickListener
     */
    public StockSection(@NonNull SectionParameters sectionParameters, ClickListener clickListener) {
        super(sectionParameters);
        this.clickListener = clickListener;
    }

    StockSection(@NonNull final String title, @NonNull final List<Stock> list,
                 @NonNull final ClickListener clickListener, Context context) {

        super(SectionParameters.builder()
                .itemResourceId(R.layout.section_stock_item)
                .headerResourceId(R.layout.section_stock_header)
                .build());

        this.title = title;
        this.mStockList = list;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return mStockList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new StockItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final StockItemViewHolder itemHolder = (StockItemViewHolder) holder;

        final Stock stock = mStockList.get(position);


        itemHolder.stockNameItemTv.setText(stock.getStockName());
        itemHolder.stockValueItemTv.setText(stock.getStockValue());
        if(stock.getStockShares() > 0){
            if(title.equals("PORTFOLIO")){


            }
            itemHolder.stockShareItemTv.setText(stock.getStockShares() + " shares");
        }else{
            itemHolder.stockShareItemTv.setText(stock.getStockDes());
        }
        float changePercent= Float.valueOf(stock.getStockTrend());
        if(changePercent >0){
            itemHolder.stockTrendItemTv.setTextColor(Color.parseColor("#319A5C"));
            itemHolder.stockTrendItemTv.setText(changePercent+ "");
            itemHolder.stockTrendItemImageView.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.ic_twotone_trending_up_24));
        }else {
            itemHolder.stockTrendItemTv.setTextColor(Color.parseColor("#B3777D"));
            itemHolder.stockTrendItemTv.setText(Math.abs(changePercent)+ "");
            itemHolder.stockTrendItemImageView.setImageDrawable(MyApplication.getAppContext().getDrawable(R.drawable.ic_baseline_trending_down_24));
        }


        itemHolder.stockDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StockDetailActivity.class);
                intent.putExtra("ticketName" , stock.getStockName());
                        context.startActivity(intent);
            }
        });

//        itemHolder.rootView.setOnClickListener(v ->
//                clickListener.onItemRootViewClicked(this, itemHolder.getAdapterPosition())
//        );
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(final View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.titleView.setText(title);
        headerHolder.titleView.setClickable(false);
        if(title.equals("PORTFOLIO")){
            final SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
            float myDollar = sharedPreferences.getFloat(MY_DOLLAR, -1);
            if (myDollar == -1) {
                sharedPreferences.edit().putFloat(MY_DOLLAR, 20000).commit();
                myDollar = 20000;
            }
            float totalVal = myDollar;
            for(Stock StockItem:mStockList) {
                totalVal += Float.valueOf(StockItem.getStockValue()) * StockItem.getStockShares();
            }
            headerHolder.expandView.setText("Net Worth\r\n" + totalVal);
        }else {
            headerHolder.expandView.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onRowMoved(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mStockList, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mStockList, i, i - 1);
//            }
//        }
//        clickListener.onRowMoved(fromPosition, toPosition);
//    }
//
//    @Override
//    public void onRowSelected(StockItemViewHolder myViewHolder) {
//        myViewHolder.rootView.setBackgroundColor(Color.GRAY);
//    }
//
//    @Override
//    public void onRowClear(StockItemViewHolder myViewHolder) {
//        myViewHolder.rootView.setBackgroundColor(Color.WHITE);
//    }

    @Override
    public void onItemDelete(int positon) {
        clickListener.remove(title,positon);
//        notifyItemRemoved(positon);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        clickListener.swap(title,fromPosition,toPosition);
//        notifyItemMoved(fromPosition,toPosition);
    }


    interface ClickListener {

//        void onItemRootViewClicked(@NonNull final StockSection section, final int itemAdapterPosition);

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(StockItemViewHolder myViewHolder);

        void onRowClear(StockItemViewHolder myViewHolder);

        void swap(String title, int fromPosition, int toPosition);

        void remove(String title, int removePostion);
    }

}
