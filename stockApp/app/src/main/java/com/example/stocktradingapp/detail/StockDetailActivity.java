package com.example.stocktradingapp.detail;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.stocktradingapp.MainActivity;
import com.example.stocktradingapp.R;
import com.example.stocktradingapp.common.Constant;
import com.example.stocktradingapp.common.MyApplication;
import com.example.stocktradingapp.entity.SearchRequestResult;
import com.example.stocktradingapp.entity.Stock;
import com.example.stocktradingapp.utils.AlterDialogNewsShow;
import com.example.stocktradingapp.utils.DateHelper;
import com.example.stocktradingapp.views.adapter.NewsArticleAdapter;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.stocktradingapp.common.Constant.FAVORITES_STOCK;
import static com.example.stocktradingapp.common.Constant.MY_DOLLAR;
import static com.example.stocktradingapp.common.Constant.PORTFOLIO_USER_OWNS;

public class StockDetailActivity extends AppCompatActivity {

    private final static String ZERO_SHARE_PORTFOLIO_TIP = "You have 0 shares of ";

    //https://stocknodebackend.wl.r.appspot.com/details?ticker=MSFT
    TextView tickerTextView,nameTextView,lastPriceTextView,changeTextView;


    WebView chartWebView;


    TextView userPortfolioTextView;
    Button tradeBtn;

    //stats section
//    TextView currentPriceTextView, lowTextView,bigPriceTextView,openPriceTextView,midTextView,highTextView,volumeTextView;
    GridView gridView;

    private BaseAdapter mAdapter = null;
    private ArrayList<String> mData = null;

    TextView aboutTextView;

    Button showMoreBtn;


    ImageView firstArticleImageView;
    TextView firstArticleTimeTextView,firstArticleTitleTextView;

    String tickerName;

    RecyclerView newRecyclerView;
    NewsArticleAdapter newsArticleAdapter;

    private LinearLayout loadingLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        setTitle("Stocks");
        tickerName = getIntent().getStringExtra("ticketName");
        loadingLinear = findViewById(R.id.detail_progress_linear);
        initUI();
    }

    private void detailJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                StockDetail stockDetail = new Gson().fromJson(String.valueOf(response), StockDetail.class);
                updateTheUI(stockDetail);
                loadingLinear.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        MyApplication.requestQueue.add(jsonObjectRequest);
    }



    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //init info section
        tickerTextView = findViewById(R.id.stock_ticker_tv_detail);
        tickerTextView.setText(tickerName);
        nameTextView = findViewById(R.id.stock_name_tv_detail);
        lastPriceTextView = findViewById(R.id.stock_value_tv_detail);
        changeTextView = findViewById(R.id.stock_change_tv_detail);

        chartWebView = findViewById(R.id.chart_web_view_detail);

        //stats
        gridView = findViewById(R.id.stats_grid_view_detail);

        aboutTextView =  findViewById(R.id.about_tv_detail);
        showMoreBtn = findViewById(R.id.show_more_btn);
        showMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showMoreBtn.getText().toString().equals("Show more...")){
                    aboutTextView.setMaxLines(100);
                    showMoreBtn.setText("Show Less");
                }else {
                    aboutTextView.setMaxLines(2);
                    showMoreBtn.setText("Show more...");
                }
            }
        });


        firstArticleTitleTextView = findViewById(R.id.first_article_title_tv_detail);
        firstArticleTimeTextView = findViewById(R.id.first_article_time_tv_detail);
        firstArticleImageView = findViewById(R.id.first_article_image_view_detail);


        mData = new ArrayList<String>();
        mData.add("1");
        mData.add("2");
        mData.add("3");
        mData.add("4");
        mData.add("5");
        mData.add("6");
        mData.add("7");

        mAdapter = new ArrayAdapter<String>(this, R.layout.grid_view_text_view_item,R.id.grid_text_view,mData);

        gridView.setAdapter(mAdapter);


        detailJsonRequest("https://stocknodebackend.wl.r.appspot.com/details?ticker=" + tickerName);



    }
    float myDollar;


    private void initSharedPreferences(String tickerName,float lastPrice) {
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myDollar = sharedPreferences.getFloat(MY_DOLLAR,-1);
        if(myDollar == -1){
            sharedPreferences.edit().putFloat(MY_DOLLAR,20000).commit();
            myDollar = 20000;
        }
        Set<String> portfolio = sharedPreferences.getStringSet(PORTFOLIO_USER_OWNS, new HashSet<String>());
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!portfolio.isEmpty() && portfolio.contains(tickerName)){
            float sharesNum = sharedPreferences.getFloat(tickerName,0);

            userPortfolioTextView.setText("Shares owned: "+ (double) Math.round(sharesNum * 100) / 100 + "\r\nMarket Value: $" + sharesNum * lastPrice);

        }else{
            userPortfolioTextView.setText("You have 0 shares of " + tickerName + ".\r\nStart trading!");
        }
        if(!favorites.isEmpty() && favorites.contains(tickerName)){

        }

    }



    void updateTheUI(StockDetail stockDetail){


        nameTextView.setText(stockDetail.getName());
        lastPriceTextView.setText("$" + stockDetail.getLast());
        float changePercent= Float.valueOf(stockDetail.getChangePercent());
        if(changePercent >0) {
            changeTextView.setText("$" + stockDetail.getChangePercent());
            changeTextView.setTextColor(Color.parseColor("#319A5C"));
        }else {
            changeTextView.setTextColor(Color.parseColor("#B3777D"));
            changeTextView.setText("-"  + "$" + Math.abs(changePercent)+ "");
        }
        //init
        userPortfolioTextView = findViewById(R.id.user_portfolio_tv_detail);

        tradeBtn = findViewById(R.id.trade_btn_detail);
        tradeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDetailActivity.this);
//                builder.setTitle("Trade " + stockDetail.getName()+" shares");
                final View customLayout = getLayoutInflater().inflate(R.layout.buy_sell_dialog_detail, null);
                TextView sharesTitle = customLayout.findViewById(R.id.sharesTitle);
                sharesTitle.setText("Trade " + stockDetail.getName()+" shares");
                EditText numberSharesEditText = customLayout.findViewById(R.id.shares_trade_edit_text_detail);
                numberSharesEditText.setKeyListener(DigitsKeyListener.getInstance("123456789."));
//                numberSharesEditText.setText("0");

                TextView costBySharesTextView = customLayout.findViewById(R.id.result_trade_by_edit_tv_detail);
                costBySharesTextView.setText("0 * $" + stockDetail.getLast() + "/share =  $0.0" );
                numberSharesEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            if (s == null || s.toString().equals("")) {

                            } else {
                                float shareNums = Float.valueOf(s.toString());
                                float price = Float.valueOf(stockDetail.getLast());
                                costBySharesTextView.setText(shareNums + " * $" + stockDetail.getLast() + "/share =  $" + (double) Math.round((shareNums * price) * 10000) / 10000);
                            }
                        }catch (Exception e){

                        }
                    }
                });
                TextView myDollarTextView = customLayout.findViewById(R.id.available_dollar_myself_tv);
                myDollarTextView.setText("$" + myDollar + " available to buy " + tickerName);
                Button buyBtn = customLayout.findViewById(R.id.buy_stock_btn_detail);
                Button sellBtn = customLayout.findViewById(R.id.sell_stock_btn_detail);
                buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(numberSharesEditText.getText().toString().equals("") || numberSharesEditText.getText().toString().equals("0")){
                            Toast.makeText(StockDetailActivity.this,"Cannot bug less than 0 shares",Toast.LENGTH_SHORT).show();
                        }else{
                            double sharesPrice = Double.valueOf(stockDetail.getLast());
                            double inputSharesNum = Float.valueOf(numberSharesEditText.getText().toString());
                            double costMoney = inputSharesNum * sharesPrice;
                            if(costMoney > myDollar){
                                Toast.makeText(StockDetailActivity.this,"Not enough money to buy",Toast.LENGTH_SHORT).show();
                            }else{
                                //buy the shares
                                final SharedPreferences sharedPreferences =
                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                                final SharedPreferences.Editor editor = sharedPreferences.edit();
                                myDollar = sharedPreferences.getFloat(MY_DOLLAR,-1);
                                myDollar -= costMoney;
                                editor.putFloat(MY_DOLLAR,myDollar).commit();
                                Set<String> portfolio = sharedPreferences.getStringSet(PORTFOLIO_USER_OWNS, new HashSet<String>());

                                if(!portfolio.isEmpty() && portfolio.contains(tickerName)){

                                }else{
                                    portfolio.add(tickerName);
                                    editor.putStringSet(PORTFOLIO_USER_OWNS,portfolio).commit();
                                }
                                float sharesNum = sharedPreferences.getFloat(tickerName,0);
                                sharesNum += inputSharesNum;
                                editor.putFloat(tickerName,sharesNum).commit();
                                userPortfolioTextView.setText("You have "+ sharesNum + " shares of " + tickerName + "\r\nMarket Value " + sharesNum * sharesPrice);
                                Dialog dialog = null;
                                AlertDialog.Builder builder = new AlertDialog.Builder(StockDetailActivity.this);
                                final View customLayout = getLayoutInflater().inflate(R.layout.congratulations_dialog_detail, null);
                                TextView congratulationTip = customLayout.findViewById(R.id.congratulation_tip);
                                Button doneBtn = customLayout.findViewById(R.id.done_btn);
                                congratulationTip.setText("You have successfully bought " + inputSharesNum + " shares of " + tickerName);

                                builder.setView(customLayout);
                                dialog = builder.create();
                                dialog.show();
                                Dialog finalDialog = dialog;
                                doneBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
                sellBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(numberSharesEditText.getText().toString().equals("") || numberSharesEditText.getText().toString().equals("0")){
                            Toast.makeText(StockDetailActivity.this,"Cannot sell less than 0 shares",Toast.LENGTH_SHORT).show();
                        }else {
                            //sell stock
                            final SharedPreferences sharedPreferences =
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                            double sharesPrice = Double.valueOf(stockDetail.getLast());
                            float inputSharesNum = Float.valueOf(numberSharesEditText.getText().toString());
                            float sharesNum = sharedPreferences.getFloat(tickerName,0);

                            if(sharesNum < inputSharesNum){
                                Toast.makeText(StockDetailActivity.this,"Not enough shares to sell",Toast.LENGTH_SHORT).show();
                            }else {
                                double sellMoney = inputSharesNum * sharesPrice;
                                myDollar = sharedPreferences.getFloat(MY_DOLLAR,-1);
                                myDollar += sellMoney;
                                editor.putFloat(MY_DOLLAR,myDollar).commit();
                                sharesNum -= inputSharesNum;
                                editor.putFloat(tickerName,sharesNum).commit();
                                if(sharesNum == 0){
                                    userPortfolioTextView.setText("You have 0 shares of " + tickerName + "\r\nStart trading");
                                    Set<String> portfolio = sharedPreferences.getStringSet(PORTFOLIO_USER_OWNS, new HashSet<String>());
                                    portfolio.remove(tickerName);
                                    editor.putStringSet(PORTFOLIO_USER_OWNS,portfolio).commit();
                                }else {
                                    userPortfolioTextView.setText("You have "+ sharesNum + " shares of " + tickerName + "\r\nMarket Value " + sharesNum * sharesPrice);
                                }
                                Dialog dialog = null;
                                AlertDialog.Builder builder = new AlertDialog.Builder(StockDetailActivity.this);
                                final View customLayout = getLayoutInflater().inflate(R.layout.congratulations_dialog_detail, null);
                                TextView congratulationTip = customLayout.findViewById(R.id.congratulation_tip);
                                Button doneBtn = customLayout.findViewById(R.id.done_btn);
                                congratulationTip.setText("You have successfully sold " + inputSharesNum + " shares of " + tickerName);

                                builder.setView(customLayout);
                                dialog = builder.create();
                                dialog.show();
                                Dialog finalDialog = dialog;
                                doneBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalDialog.dismiss();
                                    }
                                });
                            }

                        }
                    }
                });
                builder.setView(customLayout);
                Dialog dialog = builder.create();
                dialog.show();
            }
        });


        mData.clear();
        mData.add("CurrentPrice:" + stockDetail.getLast());
        mData.add("Low:" + stockDetail.getLow());
        mData.add("Bid Price:" + stockDetail.getBidPrice());
        mData.add("OpenPrice:" + stockDetail.getOpen());
        mData.add("Mid:" + stockDetail.getMid());
        mData.add("High:" + stockDetail.getHigh());
        mData.add("Volume:" + stockDetail.getVolume());
        setListViewHeightBasedOnChildren(gridView);
        mAdapter.notifyDataSetChanged();

        aboutTextView.setText(stockDetail.getDescription());

        newRecyclerView = findViewById(R.id.news_recycler_view_detail);
        if(stockDetail.getNews().size()>0) {
            News news = stockDetail.getNews().get(0);
            Glide.with(this).load(news.getUrlToImage()).into(firstArticleImageView);

            firstArticleTimeTextView.setText(news.getSourceNew() + "  " + DateUtils.getRelativeTimeSpanString(DateHelper.String2Date(news.getPublishedAt()).getTime()));
            firstArticleTitleTextView.setText(news.getTitle());
            firstArticleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(news.getUrl()));
                    startActivity(intent);
                }
            });
            firstArticleImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlterDialogNewsShow.showAlertDialogArticle(StockDetailActivity.this, news);
                    return true;
                }
            });

            List<News> otherNew = new ArrayList<News>(stockDetail.getNews());
            otherNew.remove(0);
            if(otherNew.size()>0) {
                newsArticleAdapter = new NewsArticleAdapter(StockDetailActivity.this, otherNew);

                LinearLayoutManager manager = new LinearLayoutManager(StockDetailActivity.this);
                manager.setOrientation(LinearLayoutManager.VERTICAL);
                newRecyclerView.setLayoutManager(manager);
                newRecyclerView.setAdapter(newsArticleAdapter);
                newRecyclerView.invalidate();
            }
        }


        initSharedPreferences(tickerName,Float.valueOf(stockDetail.getLast()));
        WebSettings settings = chartWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        chartWebView.clearCache(true);
        settings.setDomStorageEnabled(true);
        chartWebView.setWebViewClient(new WebViewClient());
        chartWebView.loadUrl("file:///android_asset/TickerDetail.html?tickerName="+tickerName);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());
        if (favorites.contains(tickerName)) {
            menu.getItem(0).setIcon(R.drawable.ic_baseline_star_24);
        }else {
            menu.getItem(0).setIcon(R.drawable.ic_baseline_star_border_24);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            final SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());

            if(!favorites.isEmpty() && favorites.contains(tickerName)){
                item.setIcon(R.drawable.ic_baseline_star_border_24);
                favorites.remove(tickerName);
                sharedPreferences.edit().putStringSet(FAVORITES_STOCK,favorites).commit();
                Toast.makeText(StockDetailActivity.this,"Remove " + tickerName + " to favorites successfully",Toast.LENGTH_SHORT).show();
            }else{
                item.setIcon(R.drawable.ic_baseline_star_24);
                favorites.add(tickerName);
                sharedPreferences.edit().putStringSet(FAVORITES_STOCK,favorites).commit();
                Toast.makeText(StockDetailActivity.this,"Add " + tickerName + " to favorites successfully",Toast.LENGTH_SHORT).show();
            }
            return true;
        }else if(id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // Get the Adapter for the ListView
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // Fixed column width, how many columns
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // GetCount () is less than or equal to 8 when the two heights are added
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // Gets the layout parameters of the ListView
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        listView.setLayoutParams(params);
    }
}