package by.pavel.mytutby.util.toaster;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class Toaster implements ToastProvider {

    private final Handler handler;
    private final Context appContext;

    @Inject
    public Toaster(@ApplicationContext Context context) {
        handler = new Handler(Looper.getMainLooper());
        appContext = context;
    }

    @Override
    public void showToast(int resId) {
        handler.post(() ->
                Toast.makeText(appContext, resId, Toast.LENGTH_LONG).show()
        );
    }
}
