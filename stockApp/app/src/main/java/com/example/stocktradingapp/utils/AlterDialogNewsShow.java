package com.example.stocktradingapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stocktradingapp.R;
import com.example.stocktradingapp.detail.News;
import com.example.stocktradingapp.detail.StockDetailActivity;

public class AlterDialogNewsShow {

    public static void showAlertDialogArticle(Context context, News news) {
        // create an alert builder
        final Dialog dialog = new Dialog(context);
        // set the custom layout
        final View customLayout = ((Activity)context).getLayoutInflater().inflate(R.layout.article_dialog_view, null);
        ImageView articleImageViewDialog = customLayout.findViewById(R.id.article_image_view_dialog);
        TextView articleTitleViewDialog = customLayout.findViewById(R.id.article_title_tv_dialog);
        ImageButton articleTwitterBtn = customLayout.findViewById(R.id.twitter_btn_dialog);
        ImageButton articleChromeBtn = customLayout.findViewById(R.id.chrome_btn_dialog);
        Glide.with(context).load(news.getUrlToImage()).into(articleImageViewDialog);
        articleTitleViewDialog.setText(news.getTitle());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customLayout);
        articleTwitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse( "https://twitter.com/intent/tweet?text=Check out this link:&url=" + news.getUrl()+"&hashtags=CSCI571StockAPP"));
                context.startActivity(intent);

            }
        });

        articleChromeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.getUrl()));
                context.startActivity(intent);
            }
        });

        // create and show the alert dialog

        dialog.show();
    }
}
