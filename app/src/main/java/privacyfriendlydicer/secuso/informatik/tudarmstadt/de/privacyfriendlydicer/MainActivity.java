package privacyfriendlydicer.secuso.informatik.tudarmstadt.de.privacyfriendlydicer;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                String resultString = "";
                Dicer dicer = new Dicer();
                TextView result = (TextView) findViewById(R.id.textViewResults);
                int[] dice = dicer.rollDice(poolSeekBar.getProgress() + 1);

                for (int i = 0; i < dice.length; i++) {
                    resultString += dice[i];
                }

                result.setText(resultString);
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
}
