package org.secuso.privacyfriendlydicer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by yonjuni on 15.06.16.
 */
public class HelpActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().
                replace(android.R.id.content, new HelpFragment()).
                commit();
    }
}
