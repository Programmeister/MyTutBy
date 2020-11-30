package by.pavel.mytutby.repository.feed.newfeed;

import android.content.Context;

import com.tickaroo.tikxml.TikXml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Feed;
import by.pavel.mytutby.data.feed.Html;
import by.pavel.mytutby.data.feed.Link;
import by.pavel.mytutby.network.FeedService;
import by.pavel.mytutby.util.toaster.ToastProvider;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okio.Okio;

public class AddChannelRepository implements NewFeedRepository {

    private static final String FILENAME = "feeds.txt";
    private static final String SEPARATOR = " && ";

    private final Context appContext;
    private final FeedService feedService;
    private final ExecutorService executorService;
    private final ToastProvider toastProvider;

    @Inject
    public AddChannelRepository(@ApplicationContext Context context, FeedService service, ToastProvider toaster) {
        appContext = context;
        feedService = service;
        executorService = Executors.newFixedThreadPool(1);
        toastProvider = toaster;
    }

    // TODO: Don't work
    @Override
    public List<Feed> getNewFeeds(String link) {
        Future<List<Feed>> future = executorService.submit(() -> {
            try {
                URL url = new URL(link);
                URLConnection urlConnection = url.openConnection();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//                reader.close();
//                String page = stringBuilder.toString();
                TikXml parser = new TikXml.Builder()
                        .exceptionOnUnreadXml(false)
                        .build();
                Html html = parser.read(Okio.buffer(Okio.source(urlConnection.getInputStream())), Html.class);

                List<Link> list = html.links;
                List<Feed> feeds = new ArrayList<>();
                for (Link l : list)
                    if (l.type.contains("application/rss"))
                        feeds.add(new Feed(l.title, l.href));
                return feeds;
            } catch (IOException e) {
                toastProvider.showToast(R.string.connection_problem);
                return null;
            }
        });
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addFeed(Feed feed) {
        String line = feed.title + SEPARATOR + feed.link;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                appContext.openFileOutput(FILENAME, Context.MODE_APPEND)
        ))) {
            writer.append(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
