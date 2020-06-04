package org.secuso.privacyfriendlydicer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by yonjuni on 22.10.16.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences("firstShow", getBaseContext().MODE_PRIVATE);

        Intent intent;
        if (settings.getBoolean("isFirstRun", true)) {
            intent = new Intent(SplashActivity.this, TutorialActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }

        startActivity(intent);
        finish();
    }

}
