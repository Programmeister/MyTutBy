package by.pavel.mytutby.di;

import android.content.Context;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.room.Room;
import by.pavel.mytutby.db.AppDatabase;
import by.pavel.mytutby.db.ItemDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    @NonNull
    @Provides
    @Singleton
    static AppDatabase providesDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(
                appContext,
                AppDatabase.class,
                "cache"
        ).build();
    }

    @NonNull
    @Provides
    @Singleton
    static ItemDao itemDao(@NonNull AppDatabase database) {
        return database.itemDao();
    }
}
