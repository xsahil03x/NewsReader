package com.magarex.newsreader.Interface;

import com.magarex.newsreader.Model.News;
import com.magarex.newsreader.Model.Website;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by HP on 11/12/2017.
 */

public interface NewsService {
    @GET("v1/sources?language=en")
    Call<Website> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
