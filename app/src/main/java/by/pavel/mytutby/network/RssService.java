package by.pavel.mytutby.network;

import by.pavel.mytutby.data.api.Rss;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssService {

    @GET
    Call<Rss> getRss(@Url String url);
}
