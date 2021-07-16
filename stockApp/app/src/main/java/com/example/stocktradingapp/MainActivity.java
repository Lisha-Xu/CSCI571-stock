package com.example.stocktradingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.stocktradingapp.common.MyApplication;
import com.example.stocktradingapp.detail.StockDetail;
import com.example.stocktradingapp.detail.StockDetailActivity;
import com.example.stocktradingapp.entity.SearchRequestResult;
import com.example.stocktradingapp.entity.Stock;
import com.example.stocktradingapp.entity.TickInfo;
import com.example.stocktradingapp.views.RecycleItemTouchHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.example.stocktradingapp.common.Constant.FAVORITES_STOCK;
import static com.example.stocktradingapp.common.Constant.MY_DOLLAR;
import static com.example.stocktradingapp.common.Constant.PORTFOLIO_USER_OWNS;

public class MainActivity extends AppCompatActivity implements StockSection.ClickListener {
    LinkTextView linkTextView;
    TextView curDateTextView;

    List<Stock> listPortfolio = new ArrayList<>();
    List<Stock> listFavorites = new ArrayList<>();
    float myDollar;
    private SectionedRecyclerViewAdapter sectionedAdapter;
    private LinearLayout loadingLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleColor(Color.BLACK);
        setTitle(R.string.stock_title);
        initUI();
    }
    public void setTitleColor(int textColor) {
        super.setTitleColor(Color.BLACK);
    }
    void initUI() {
        linkTextView = findViewById(R.id.link_tiingo_tv);
        linkTextView.setUrl("Powered by tiingo","https://www.tiingo.com/");
        curDateTextView = findViewById(R.id.cur_date_tv);
        //init the current date
        Date curDate = new Date(System.currentTimeMillis()-8*3600*1000);
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        curDateTextView.setText((String)df.format(curDate));

        loadingLinear = findViewById(R.id.main_progress_linear);
//        loading = new ProgressDialog(MainActivity.this,R.layout.fetch_dialog_view);
//
//        loading.setCancelable(true);
//        loading.setMessage(Constant.FETCHING_DATA);
//        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        loading.show();

        sectionedAdapter = new SectionedRecyclerViewAdapter();

        Map<String, List<Stock>> stockMap = new HashMap<>();

        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> portfolio = sharedPreferences.getStringSet(PORTFOLIO_USER_OWNS, new HashSet<String>());
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());
        final SharedPreferences.Editor editor = sharedPreferences.edit();

//        editor.putFloat("MSFT",1).commit();
//        portfolio.add("MSFT");
//        portfolio.add("GOOGL");
//        portfolio.add("AAPL");
//        favorites.add("NFLX");
//        favorites.add("GOOGL");
//        favorites.add("AAPL");
//        favorites.add("TSLA");
//        favorites.add("IBM");
//        favorites.add("AMZN");
//        editor.putStringSet(PORTFOLIO_USER_OWNS, portfolio).commit();
//        editor.putStringSet(FAVORITES_STOCK, favorites).commit();

        myDollar = sharedPreferences.getFloat(MY_DOLLAR, -1);
        if (myDollar == -1) {
            sharedPreferences.edit().putFloat(MY_DOLLAR, 20000).commit();
            myDollar = 20000;
        }
        Iterator<String> iterator = portfolio.iterator();
        while (iterator.hasNext()) {
            String tickerName = iterator.next();
            if(sharedPreferences.getFloat(tickerName, 0) > 0) {
                listPortfolio.add(new Stock(tickerName, "0", sharedPreferences.getFloat(tickerName, 0), "0"));
            }
        }


        iterator = favorites.iterator();
        while (iterator.hasNext()) {
            String tickerName = iterator.next();
            listFavorites.add(new Stock(tickerName, "0", sharedPreferences.getFloat(tickerName, 0), "0"));
        }
//        stockMap.put("Portfolio",listPortfolio);
//        stockMap.put("Favorites",listFavorites);

        sectionedAdapter.addSection(new StockSection("PORTFOLIO", listPortfolio, this, MainActivity.this));
        sectionedAdapter.addSection(new StockSection("FAVORITES", listFavorites, this, MainActivity.this));


        final RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(sectionedAdapter);
        ItemTouchHelper.Callback callback=new RecycleItemTouchHelper((StockSection)sectionedAdapter.getSection(0));
        ItemTouchHelper.Callback callback1=new RecycleItemTouchHelper((StockSection)sectionedAdapter.getSection(1));
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        initData();
        runnable.run();
        if(favorites.size() == 0 && listFavorites.size()==0){
            loadingLinear.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> portfolio = sharedPreferences.getStringSet(PORTFOLIO_USER_OWNS, new HashSet<String>());
        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());
        listPortfolio.clear();
        Iterator<String> iterator = portfolio.iterator();
        while (iterator.hasNext()) {
            String tickerName = iterator.next();
            if(sharedPreferences.getFloat(tickerName, 0) > 0) {
                listPortfolio.add(new Stock(tickerName, "0", sharedPreferences.getFloat(tickerName, 0), "0"));
            }
        }
        listFavorites.clear();
        iterator = favorites.iterator();
        while (iterator.hasNext()) {
            String tickerName = iterator.next();
            listFavorites.add(new Stock(tickerName, "0", sharedPreferences.getFloat(tickerName, 0), "0"));
        }
        sectionedAdapter.notifyDataSetChanged();
        count = -1;
        runnable.run();
    }

    private void initData() {
        for (Stock stock : listPortfolio) {
            testDetailJsonRequest("https://stocknodebackend.wl.r.appspot.com/watchlistback?ticker=" + stock.getStockName());
        }
        for (Stock stock : listFavorites) {
            testDetailJsonRequest("https://stocknodebackend.wl.r.appspot.com/watchlistback?ticker=" + stock.getStockName());
        }

    }
//    @Override
//    public void onItemRootViewClicked(@NonNull StockSection section, int itemAdapterPosition) {
//        Toast.makeText(MainActivity.this,section.title + "  " + itemAdapterPosition,Toast.LENGTH_LONG).show();
//
//    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        sectionedAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(StockItemViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(StockItemViewHolder myViewHolder) {

    }

    @Override
    public void swap(String title, int fromPosition, int toPosition) {
        try {
            System.out.println(title + "  " + fromPosition + "  " + toPosition);
            if (fromPosition == listPortfolio.size() + 1 || fromPosition == 0 ) {

            } else if (fromPosition <= listPortfolio.size() ) {
                if(toPosition == 0){
                    toPosition = 1;
                }
                Collections.swap(listPortfolio, fromPosition - 1, toPosition -1);
            } else {
                if(toPosition == listPortfolio.size() + 1) {
                    toPosition = listPortfolio.size()+2;
                }
                Collections.swap(listFavorites, fromPosition - 2 - listPortfolio.size(), toPosition - 2 - listPortfolio.size());
            }
            sectionedAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }
    }

    @Override
    public void remove(String title, int removePostion) {

        System.out.println(title + "  " +  removePostion );
        if(removePostion==listPortfolio.size()+1 || removePostion==0){

        }else if(removePostion <= listPortfolio.size()){
            listPortfolio.remove(removePostion-1);
        }else {
            Stock removeStock = listFavorites.remove(removePostion-2-listPortfolio.size());
            final SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_STOCK, new HashSet<String>());
            favorites.remove(removeStock.getStockName());
            sharedPreferences.edit().putStringSet(FAVORITES_STOCK,favorites).commit();

        }
        sectionedAdapter.notifyDataSetChanged();
    }

    int count = 1;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(count <= 0){
                for (Stock stock : listPortfolio) {
                    testDetailJsonRequest("https://stocknodebackend.wl.r.appspot.com/watchlistback?ticker=" + stock.getStockName());
                }
                for (Stock stock : listFavorites) {
                    testDetailJsonRequest("https://stocknodebackend.wl.r.appspot.com/watchlistback?ticker=" + stock.getStockName());
                }
            }
//            Log.d("Runnable","every 15 seonds ");
//            mHandler.postDelayed(this, 15000);
        }
    };


    private void detailFirstJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                List<SearchRequestResult> searchRequestResultList = new ArrayList<>();
                StockDetail stockDetail = new Gson().fromJson(String.valueOf(response), StockDetail.class);
                float totalVal = 0;
                for (Stock stock : listPortfolio) {
                    if (stock.getStockName().equals(stockDetail.getTicker())) {
                        stock.setStockTrend(stockDetail.getChangePercent());
                        stock.setStockValue(stockDetail.getLast());
                        stock.setStockDes(stockDetail.getName());
                    }
                }
                for (Stock stock : listFavorites) {
                    if (stock.getStockName().equals(stockDetail.getTicker())) {
                        stock.setStockTrend(stockDetail.getChangePercent());
                        stock.setStockValue(stockDetail.getLast());
                        stock.setStockDes(stockDetail.getName());
                    }
                }
                count --;
                sectionedAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        MyApplication.requestQueue.add(jsonObjectRequest);
    }

    private void testDetailJsonRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                List<SearchRequestResult> searchRequestResultList = new ArrayList<>();
//                Log.d("seconds 15 flush",response.toString());
                TickInfo stockDetail = new Gson().fromJson(String.valueOf(response), TickInfo.class);
                float totalVal = 0;
                for (Stock stock : listPortfolio) {
                    if (stock.getStockName().equals(stockDetail.getTicker())) {
                        stock.setStockTrend(stockDetail.getChangePercent());
                        stock.setStockValue(stockDetail.getLast());
                        stock.setStockDes(stockDetail.getName());
                    }
                    totalVal += Float.valueOf(stock.getStockValue());
                }
                totalVal = myDollar + totalVal;
                for (Stock stock : listFavorites) {
                    if (stock.getStockName().equals(stockDetail.getTicker())) {
                        stock.setStockTrend(stockDetail.getChangePercent());
                        stock.setStockValue(stockDetail.getLast());
                        stock.setStockDes(stockDetail.getName());
                    }
                }
                count --;
                sectionedAdapter.notifyDataSetChanged();
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

    private void testSearchJsonRequest(String url) {
        StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonArray jsonArray = parser.parse(response).getAsJsonArray();
                    Gson gson = new Gson();
                    List<SearchRequestResult> searchRequestResultList = new ArrayList<>();
                    for (JsonElement searchResult : jsonArray) {
                        SearchRequestResult searchRequestResult = gson.fromJson(searchResult, SearchRequestResult.class);
                        searchRequestResultList.add(searchRequestResult);
                    }
                    List<String> list = new ArrayList<>();
                    if (searchRequestResultList != null) {
                        for (SearchRequestResult r : searchRequestResultList) {
                            list.add(r.getTicker() + "-" + r.getName());
//                            Log.d("##result##", "ticker:" + r.getTicker() + "name:" + r.getName() + "\n");
                        }
                        dataArr.clear();
                        dataArr.addAll(list);
                        newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
                        searchAutoComplete.setAdapter(newsAdapter);
                        newsAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Log.e("error",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });

        MyApplication.requestQueue.add(sr);
    }
    List<String> dataArr = new ArrayList<String>();
    ArrayAdapter<String> newsAdapter;
    SearchView.SearchAutoComplete searchAutoComplete;
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar_search_example_menu, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        // Get SearchView autocomplete object.
        searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setHint("");
        searchView.setIconifiedByDefault(false);
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setThreshold(3);
        // Create a new ArrayAdapter and add data to search auto complete object.
        newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                Intent intent = new Intent(MainActivity.this, StockDetailActivity.class);
                intent.putExtra("ticketName" ,(String) ((String) adapterView.getItemAtPosition(itemIndex)).split("-")[0]);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 3){
                    testSearchJsonRequest("https://stocknodebackend.wl.r.appspot.com/info?searchValue=" + newText);
                }
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}