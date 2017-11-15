package com.magarex.newsreader;

import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.magarex.newsreader.Adapter.ListSourceAdapter;
import com.magarex.newsreader.Common.Common;
import com.magarex.newsreader.Interface.NewsService;
import com.magarex.newsreader.Model.Website;

import dmax.dialog.SpotsDialog;
import io.paperdb.Book;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init cache
        Paper.init(this);

        //init Service
        mService = Common.getNewsService();

        //init View
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        listWebsite = (RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);

        loadWebsiteSource(false);

    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache != null && !cache.isEmpty()) //if have cache
            {
                Website website = new Gson().fromJson(cache,Website.class); //converting cache from json to object
                adapter = new ListSourceAdapter(getBaseContext(),website);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }
            else //cache is empty
            {
                dialog.show();
                //Fetch new Data
                mService.getSources().enqueue(new Callback<Website>() {
                    @Override
                    public void onResponse(Call<Website> call, Response<Website> response) {
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //saving to cache
                        Paper.book().write("cache",new Gson().toJson(response.body()));

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Website> call, Throwable t) {

                    }
                });

            }
        }
        else //if from swipe to refresh
        {
            dialog.show();
            //Fetch new Data
            mService.getSources().enqueue(new Callback<Website>() {
                @Override
                public void onResponse(Call<Website> call, Response<Website> response) {
                    adapter = new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //saving to cache
                    Paper.book().write("cache",new Gson().toJson(response.body()));

                    //stop refresh progress
                    swipeLayout.setRefreshing(false);

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<Website> call, Throwable t) {

                }
            });

        }

    }
}

