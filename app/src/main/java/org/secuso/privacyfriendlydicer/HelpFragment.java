package org.secuso.privacyfriendlydicer;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class HelpFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.help);
    }

}