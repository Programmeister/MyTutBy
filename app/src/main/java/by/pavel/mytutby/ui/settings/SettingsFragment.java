package by.pavel.mytutby.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import by.pavel.mytutby.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
