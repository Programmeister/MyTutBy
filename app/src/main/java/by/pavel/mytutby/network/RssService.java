package by.pavel.mytutby.network;

import by.pavel.mytutby.data.api.Rss;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RssService {

    @GET("/rss/index.rss")
    Call<Rss> getRss();
}
