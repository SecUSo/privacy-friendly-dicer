package org.secuso.privacyfriendlydicer;

        import android.os.Bundle;
        import android.preference.PreferenceFragment;

/**
 * Created by yonjuni on 31.08.15.
 */
public class SettingsFragment extends PreferenceFragment{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }


}
