package by.pavel.mytutby.di;

import javax.inject.Singleton;

import by.pavel.mytutby.repository.feed.ChannelRepository;
import by.pavel.mytutby.repository.feed.FeedRepository;
import by.pavel.mytutby.repository.feed.newfeed.AddChannelRepository;
import by.pavel.mytutby.repository.feed.newfeed.NewFeedRepository;
import by.pavel.mytutby.repository.item.DetailsRepository;
import by.pavel.mytutby.repository.item.ItemRepository;
import by.pavel.mytutby.repository.list.ItemsRepository;
import by.pavel.mytutby.repository.list.ListRepository;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract ListRepository bindListRepository(ItemsRepository repository);

    @Binds
    @Singleton
    abstract ItemRepository bindItemRepository(DetailsRepository repository);

    @Binds
    @Singleton
    abstract FeedRepository bindFeedRepository(ChannelRepository repository);

    @Binds
    @Singleton
    abstract NewFeedRepository bindNewFeedRepository(AddChannelRepository repository);
}
