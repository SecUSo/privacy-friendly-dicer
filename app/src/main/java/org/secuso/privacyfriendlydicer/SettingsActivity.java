package org.secuso.privacyfriendlydicer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yonjuni on 15.06.16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().
                replace(android.R.id.content, new SettingsFragment()).
                commit();
    }
}