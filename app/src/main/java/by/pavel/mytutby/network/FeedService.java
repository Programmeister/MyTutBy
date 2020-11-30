package by.pavel.mytutby.network;

import by.pavel.mytutby.data.feed.Html;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface FeedService {

    @GET
    Call<Html> getFeedsLinks(@Url String url);
}
