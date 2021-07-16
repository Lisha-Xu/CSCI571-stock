package com.example.stocktradingapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.AttributeSet;
import android.widget.TextView;

public class LinkTextView extends androidx.appcompat.widget.AppCompatTextView implements OnClickListener{
    String urlText;
    String uiText;
    public LinkTextView(Context context) {
        super(context);
        setClickable(true);
        this.setOnClickListener(this);
        // TODO Auto-generated constructor stub
    }
    public LinkTextView(Context context,AttributeSet attrs){
        super(context,attrs);
        setClickable(true);
        this.setOnClickListener(this);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Uri uri = Uri.parse(urlText);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        v.getContext().startActivity(intent);
    }
    public void setUrl(String uiText, String urlText){
        this.uiText = uiText;
        this.urlText=urlText;
        this.setText(uiText);
    }
    private int getTextWidth(){
        int len=urlText.length();
        return len*6;
    }


}
