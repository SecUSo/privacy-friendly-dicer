package privacyfriendlydicer.secuso.informatik.tudarmstadt.de.privacyfriendlydicer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Button rollDiceButton = (Button) findViewById(R.id.rollButton);

        final SeekBar poolSeekBar = (SeekBar) findViewById(R.id.seekBar);

        SeekBar seekBarLength = (SeekBar) findViewById(R.id.seekBar);

        seekBarLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        rollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dicer dicer = new Dicer();
                int[] dice = dicer.rollDice(poolSeekBar.getProgress() + 1);
                initResultDiceViews();

                shakingEnabled = sharedPreferences.getBoolean("enable_shaking", true);
                vibrationEnabled = sharedPreferences.getBoolean("enable_vibration", true);

                final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                for (int i = 0; i < dice.length; i++) {
                        switchDice(imageViews[i], dice[i]);
                        if (vibrationEnabled) {
                            vibrator.vibrate(50);}
                }
            }
        });

        // ShakeDetector initialization
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new ShakeListener();
        shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake(int count) {

                Dicer dicer = new Dicer();
                int[] dice = dicer.rollDice(poolSeekBar.getProgress() + 1);
                initResultDiceViews();

                if (shakingEnabled) {
                    final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    for (int i = 0; i < dice.length; i++) {
                        switchDice(imageViews[i], dice[i]);
                        if (vibrationEnabled) {
                            vibrator.vibrate(50);
                        }
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();
        intent.setClass(this, Preferences.class);
        startActivityForResult(intent, 0);

        return true;
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
                imageView.setImageResource(R.drawable.ws1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ws2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ws3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ws4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ws5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ws6);
                break;
            case 0:
                imageView.setImageResource(0);
                break;
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeListener, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        shakingEnabled = sharedPreferences.getBoolean("enable_shaking", true);
        vibrationEnabled = sharedPreferences.getBoolean("enable_vibration", true);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }
}
