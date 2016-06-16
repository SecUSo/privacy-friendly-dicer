package org.secuso.privacyfriendlydicer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView[] imageViews;
    boolean shakingEnabled;
    boolean vibrationEnabled;
    SharedPreferences sharedPreferences;

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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            flashResult(imageViews[i]);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
