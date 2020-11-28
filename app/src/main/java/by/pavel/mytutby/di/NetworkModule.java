package by.pavel.mytutby.di;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import by.pavel.mytutby.network.RssService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {

    @NonNull
    @Provides
    @Singleton
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://news.tut.by")
                .addConverterFactory(TikXmlConverterFactory.create(
                        new TikXml.Builder()
                                .exceptionOnUnreadXml(false)
                                .build()))
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    static RssService provideRssService(@NonNull Retrofit retrofit) {
        return retrofit.create(RssService.class);
    }
}
