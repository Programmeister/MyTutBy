package by.pavel.mytutby.repository.feed.newfeed;

import android.content.Context;

import com.tickaroo.tikxml.TikXml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddChannelRepository implements NewFeedRepository {

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
    public List<Feed> getNewFeeds(String url) {
        Future<List<Feed>> future = executorService.submit(() -> {
            try {
                Call<ResponseBody> call = feedService.getFeedsLinks(url);
                ResponseBody body = call.execute().body();
                if (body == null)
                    return null;

                String s = body.string();

                TikXml parser = new TikXml.Builder().exceptionOnUnreadXml(false).build();
                Html html = parser.read(body.source(), Html.class);
                List<Link> list = html.links;
                List<Feed> feeds = new ArrayList<>();
                for (Link link : list)
                    if (link.type.contains("application/rss"))
                        feeds.add(new Feed(link.title, link.href));
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
        String fileName = appContext.getString(R.string.file_name);
        String separator = appContext.getString(R.string.separator);
        String line = feed.title + separator + feed.link;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                appContext.openFileInput(fileName)
        ))) {
            String str;
            while ((str = reader.readLine()) != null)
                if (line.equals(str))
                    return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                appContext.openFileOutput(fileName, Context.MODE_APPEND)
        ))) {
            writer.append(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
