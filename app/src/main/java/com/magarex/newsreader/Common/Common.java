package com.magarex.newsreader.Common;

import com.magarex.newsreader.Interface.IconBetterIdeaService;
import com.magarex.newsreader.Interface.NewsService;
import com.magarex.newsreader.Remote.IconBetterIdeaClient;
import com.magarex.newsreader.Remote.RetrofitClient;

/**
 * Created by HP on 11/12/2017.
 */

public class Common {
    public static final String BASE_URL="https://newsapi.org/";

    public static final String API_KEY="a4b97f5dd7dd4798bfee7067a3ec323b";

    public static NewsService getNewsService()
    {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService()
    {
        return IconBetterIdeaClient.getClient().create(IconBetterIdeaService.class);
    }

    //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=a4b97f5dd7dd4798bfee7067a3ec323b

    public static String getAPIUrl(String source,String sortBy,String API_KEY)
    {
        StringBuilder apiUrl = new StringBuilder(" https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(API_KEY)
                .toString();
    }
}
