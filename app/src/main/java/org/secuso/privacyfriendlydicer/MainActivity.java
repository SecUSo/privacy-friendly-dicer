package org.secuso.privacyfriendlydicer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private ImageView[] imageViews;
    boolean shakingEnabled;
    boolean vibrationEnabled;
    SharedPreferences sharedPreferences;

    // for Shaking
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener shakeListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name_long);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#024265")));

        //Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Seekbar
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

        //Button
        Button rollDiceButton = (Button) findViewById(R.id.rollButton);

        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), poolSeekBar.getProgress() + 1);

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
                    evaluate((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), poolSeekBar.getProgress() + 1);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent.setClass(this, PreferencesActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.about:
                intent.setClass(this, AboutActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


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

        switch (result) {
            case 1:
                imageView.setImageResource(R.drawable.d1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.d2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.d3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.d4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.d5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.d6);
                break;
            case 0:
                imageView.setImageResource(0);
                break;
            default:
                break;
        }

    }

    public void evaluate(Vibrator vibrator, int diceNumber) {

        applySettings();

        Dicer dicer = new Dicer();
        int[] dice = dicer.rollDice(diceNumber);
        initResultDiceViews();

        Display display = getWindowManager().getDefaultDisplay();

        for (int i = 0; i < dice.length; i++) {
            switchDice(imageViews[i], dice[i]);
            android.view.ViewGroup.LayoutParams layoutParams = imageViews[i].getLayoutParams();
            layoutParams.width = display.getWidth() / 6;
            layoutParams.height = display.getWidth() / 6;

            imageViews[i].setLayoutParams(layoutParams);
            if (vibrationEnabled) {
                vibrator.vibrate(50);
            }

        }

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
}