package by.pavel.mytutby.di;

import by.pavel.mytutby.util.toaster.ToastProvider;
import by.pavel.mytutby.util.toaster.Toaster;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public abstract class UtilModule {

    @Binds
    abstract ToastProvider bindToastProvider(Toaster toaster);
}
