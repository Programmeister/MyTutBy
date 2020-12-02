package by.pavel.mytutby.repository.feed.newfeed;

import android.content.Context;
import android.util.Log;

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
import by.pavel.mytutby.network.FeedService;
import by.pavel.mytutby.util.toaster.ToastProvider;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AddChannelRepository implements NewFeedRepository {

    private final Context appContext;
    private final FeedService feedService;
    private final ToastProvider toastProvider;
    private final ExecutorService executorService;

    @Inject
    public AddChannelRepository(@ApplicationContext Context context,
                                FeedService service,
                                ToastProvider toaster) {
        appContext = context;
        feedService = service;
        toastProvider = toaster;
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public List<Feed> getNewFeeds(String url) {
        Future<List<Feed>> future = executorService.submit(() -> {
            try {
                Call<ResponseBody> call = feedService.getFeedsLinks(url);
                ResponseBody body = call.execute().body();
                if (body != null)
                    return parseFeedsFromHtml(body.string());
            } catch (IOException e) {
                e.printStackTrace();
                toastProvider.showToast(R.string.connection_problem);
            }
            return null;
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

        if (!isLineRecorded(fileName, line))
            writeLine(fileName, line);
    }

    private List<Feed> parseFeedsFromHtml(String htmlString) {
        List<Feed> feeds = new ArrayList<>();

        String[] strings = htmlString.substring(
                htmlString.indexOf("<head"),
                htmlString.indexOf("</head>")
        ).split("<");

        for (String s : strings) {
            if (s.contains("link") && s.contains("rss")) {
                int i1 = s.indexOf("title=") + 7;
                int i2 = s.indexOf('"', i1);
                String title = s.substring(i1, i2);
                i1 = s.indexOf("href=") + 7;
                i2 = s.indexOf('"', i1);
                String href = s.substring(i1, i2);
                feeds.add(new Feed(title, href));
                Log.d("RESULT", title + " ## " + href);
            }
        }
        return feeds;
    }

    private boolean isLineRecorded(String fileName, String line) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                appContext.openFileInput(fileName)
        ))) {
            String str;
            while ((str = reader.readLine()) != null)
                if (line.equals(str))
                    return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void writeLine(String fileName, String line) {
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
