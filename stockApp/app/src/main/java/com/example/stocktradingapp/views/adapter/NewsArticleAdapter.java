package com.example.stocktradingapp.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stocktradingapp.R;
import com.example.stocktradingapp.detail.News;
import com.example.stocktradingapp.utils.AlterDialogNewsShow;
import com.example.stocktradingapp.utils.DateHelper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.ViewHolder> {

    private List<News> mNews;
    private Context context;

    public NewsArticleAdapter(Context context, List<News> mNews) {
        this.context = context;
        this.mNews = mNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater
                                .from(parent.getContext()).inflate(R.layout.news_section_item_detail, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.newsDateDesTv.setText(mNews.get(position).getSourceNew() + "  " +DateHelper.dateBeforeCurrent(DateHelper.String2Date(mNews.get(position).getPublishedAt())));
        holder.newsTitleTv.setText(mNews.get(position).getTitle());
        holder.newsTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mNews.get(position).getUrl()));
                context.startActivity(intent);
            }
        });
        holder.newsTitleTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlterDialogNewsShow.showAlertDialogArticle(context,mNews.get(position));
                return true;
            }
        });
        Glide.with(context).load(mNews.get(position).getUrlToImage()).into(holder.newsItemImageView);
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView newsTitleTv;
        final TextView newsDateDesTv;
        final ImageView newsItemImageView;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            newsTitleTv = (TextView) itemView.findViewById(R.id.article_title_item_tv);
            newsDateDesTv = (TextView) itemView.findViewById(R.id.article_time_remark_item_tv);
            newsItemImageView = (ImageView) itemView.findViewById(R.id.article_item_image_view);
        }
    }
}
