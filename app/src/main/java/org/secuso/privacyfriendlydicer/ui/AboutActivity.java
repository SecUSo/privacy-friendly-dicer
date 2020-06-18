package org.secuso.privacyfriendlydicer.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.secuso.privacyfriendlydicer.BuildConfig;
import org.secuso.privacyfriendlydicer.R;

/**
 * Created by yonjuni on 15.06.16.
 */
public class AboutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((TextView)findViewById(R.id.secusoWebsite)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.githubURL)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.textFieldVersion)).setText(getString(R.string.version_number, BuildConfig.VERSION_NAME));

    }

}
