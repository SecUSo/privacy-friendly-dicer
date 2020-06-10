package org.secuso.privacyfriendlydicer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.DrawableUtils;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Dicer dicer = new Dicer();

    private ImageView[] imageViews;
    private boolean shakingEnabled;
    private boolean vibrationEnabled;
    private SharedPreferences sharedPreferences;

    // for Shaking
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener shakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Seekbars
        final SeekBar poolSeekBar = (SeekBar) findViewById(R.id.seekBar);

        poolSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textViewLengthDisplay =
                        (TextView) findViewById(R.id.chooseDiceNumber);
                textViewLengthDisplay.setText(Integer.toString(progress + 1));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final SeekBar facesSeekBar = (SeekBar) findViewById(R.id.seekBarFace);

        facesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textViewLengthDisplay =
                        (TextView) findViewById(R.id.chooseFaceNumber);
                textViewLengthDisplay.setText(Integer.toString(progress + 1));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Button
        Button rollDiceButton = (Button) findViewById(R.id.rollButton);

        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), poolSeekBar.getProgress() + 1, facesSeekBar.getProgress() + 1);

            }
        });

        //Shaking
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new ShakeListener();
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake(int count) {

                if (shakingEnabled) {
                    evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), poolSeekBar.getProgress() + 1, facesSeekBar.getProgress() + 1);
                }
            }
        });

        displaySum(new int[]{0});
        initResultDiceViews();
    }

    public void flashResult(ImageView imageView) {

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animation.setStartOffset(20);
        animation.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(animation);

    }

    public void initResultDiceViews() {
        imageViews = new ImageView[10];

        imageViews[0] = (ImageView) findViewById(R.id.resultOne);
        imageViews[1] = (ImageView) findViewById(R.id.resultTwo);
        imageViews[2] = (ImageView) findViewById(R.id.resultThree);
        imageViews[3] = (ImageView) findViewById(R.id.resultFour);
        imageViews[4] = (ImageView) findViewById(R.id.resultFive);
        imageViews[5] = (ImageView) findViewById(R.id.resultSix);
        imageViews[6] = (ImageView) findViewById(R.id.resultSeven);
        imageViews[7] = (ImageView) findViewById(R.id.resultEight);
        imageViews[8] = (ImageView) findViewById(R.id.resultNine);
        imageViews[9] = (ImageView) findViewById(R.id.resultTen);

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setImageResource(0);
        }
    }

    public void switchDice(ImageView imageView, int result) {

        imageView.setImageResource(this.getResources().getIdentifier("d"+result, "drawable", this.getPackageName()));
    }

    public void evaluate(Vibrator vibrator, int diceNumber, int faceNumber) {

        applySettings();

        int[] dice = dicer.rollDice(diceNumber, faceNumber);

        displaySum(dice);
        initResultDiceViews();
        showDice(dice);

        if (vibrationEnabled) {
            vibrator.vibrate(50);
        }
    }

    private void showDice(int[] dice) {
        for (int i = 0; i < dice.length; i++) {
            switchDice(imageViews[i], dice[i]);
            flashResult(imageViews[i]);
        }
    }

    private void displaySum(int[] dice) {
        int sum = 0;
        for (int d : dice) {
            sum += d;
        }
        TextView sumTextView = findViewById(R.id.sumTextView);
        sumTextView.setText(getString(R.string.main_dice_sum, Integer.toString(sum)));
    }

    public void applySettings() {
        shakingEnabled = sharedPreferences.getBoolean("enable_shaking", true);
        vibrationEnabled = sharedPreferences.getBoolean("enable_vibration", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeListener, accelerometer,
                SensorManager.SENSOR_DELAY_UI);

        applySettings();

    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;

        switch (item.getItemId()) {
            case R.id.nav_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.nav_help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return true;

            case R.id.nav_settimgs:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.nav_tutorial:
                intent = new Intent(this, TutorialActivity.class);
                startActivity(intent);
                return true;

            default:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
