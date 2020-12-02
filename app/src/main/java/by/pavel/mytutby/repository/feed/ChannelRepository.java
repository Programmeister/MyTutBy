package by.pavel.mytutby.repository.feed;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.pavel.mytutby.R;
import by.pavel.mytutby.data.Feed;
import dagger.hilt.android.qualifiers.ApplicationContext;

import static android.content.Context.MODE_PRIVATE;

public class ChannelRepository implements FeedRepository {

    private final Context appContext;

    @Inject
    public ChannelRepository(@ApplicationContext Context context) {
        appContext = context;
    }

    @Override
    public List<Feed> getFeeds() {
        String fileName = appContext.getString(R.string.file_name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                appContext.openFileInput(fileName)
        ))) {
            List<Feed> feeds = new ArrayList<>();
            String str;
            String separator = appContext.getString(R.string.separator);
            while ((str = reader.readLine()) != null) {
                int pos = str.indexOf(separator);
                feeds.add(new Feed(str.substring(0, pos), str.substring(pos + 4)));
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
            String fileName = appContext.getString(R.string.file_name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    appContext.openFileInput(appContext.getString(R.string.file_name))
            ));
            List<String> list = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null)
                if (!line.contains(title))
                    list.add(line);
            reader.close();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    appContext.openFileOutput(fileName, MODE_PRIVATE)
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
