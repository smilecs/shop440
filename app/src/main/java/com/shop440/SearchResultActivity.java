package com.shop440;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.shop440.Adapters.ProductAdapter;
import com.shop440.api.Urls;
import com.shop440.models.Datum;
import com.shop440.utils.EndlessRecyclerViewScrollListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    String query;
    String page;
    RecyclerView list;
    ProductAdapter mainAdapter;
    ArrayList<Datum> model;
    Context c;
    StaggeredGridLayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    Boolean next = true;
    TextView feedback;
    SwipeRefreshLayout view;
    String TAG = "SearchResultActivity";
    String URI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        c = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query);
        if(getIntent().hasExtra("title")){
            getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        }
        view = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        page = "1";
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        model = new ArrayList<>();
        mainAdapter = new ProductAdapter(c, model);
        list = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        list.setHasFixedSize(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(mainAdapter);
        list.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, String.valueOf(page));
                if(next){
                }
            }
        });
       }


    public String getUrl(String page, String q){
        if(getIntent().getBooleanExtra("isSearch", true)){
            try{
                URI = Urls.BASE_URL + Urls.GETPRODUCTS +"?query=" + URLEncoder.encode(q, "UTF-8") + "&p="+page;
            }catch (UnsupportedEncodingException un){
                un.printStackTrace();
            }
        }else {
            try{
                URI = Urls.BASE_URL + Urls.GETPRODUCTS +"?category=" + URLEncoder.encode(q, "UTF-8") + "&p="+page;
            }catch (UnsupportedEncodingException un){
                un.printStackTrace();
            }
        }
        return URI;
    }
}
