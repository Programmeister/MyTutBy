package by.pavel.mytutby.repository.feed;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.pavel.mytutby.data.Feed;
import dagger.hilt.android.qualifiers.ApplicationContext;

import static android.content.Context.MODE_PRIVATE;

public class ChannelRepository implements FeedRepository {

    private static final String FILENAME = "feeds.txt";
    private static final String LOG_TAG = "FILE_WORK_TAG";
    private static final String SEPARATOR = " && ";

    private final Context appContext;

    @Inject
    public ChannelRepository(@ApplicationContext Context context) {
        appContext = context;
    }

    @Override
    public List<Feed> getFeeds() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                appContext.openFileInput(FILENAME)
        ))) {
            List<Feed> feeds = new ArrayList<>();
            String str = "";
            while ((str = bufferedReader.readLine()) != null) {
                int pos = str.indexOf(SEPARATOR);
                feeds.add(new Feed(str.substring(0, pos), str.substring(pos + 4)));
                Log.d(LOG_TAG, str);
            }
            return feeds;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteFeed(String title) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    appContext.openFileInput(FILENAME)
            ));
            List<String> list = new ArrayList<>();
            String line = "";
            while ((line = reader.readLine()) != null)
                if (!line.contains(title))
                    list.add(line);
            reader.close();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    appContext.openFileOutput(FILENAME, MODE_PRIVATE)
            ));
            for (String s : list) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
