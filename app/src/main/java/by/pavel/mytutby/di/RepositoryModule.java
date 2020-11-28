package by.pavel.mytutby.di;

import javax.inject.Singleton;

import by.pavel.mytutby.repository.ListRepository;
import by.pavel.mytutby.repository.Repository;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract Repository bindRepository(ListRepository repository);
}
