package by.pavel.mytutby.util.dateformatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class MyDateFormatter implements DateFormatter {

    private final SimpleDateFormat format;

    @Inject
    public MyDateFormatter() {
        format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
    }

    @Override
    public long getTimeInMillis(String stringDate) {
        long timeInMillis;
        try {
            Date date = format.parse(stringDate);
            timeInMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            timeInMillis = Calendar.getInstance().getTimeInMillis();
        }
        return timeInMillis;
    }
}
