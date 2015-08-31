package privacyfriendlydicer.secuso.informatik.tudarmstadt.de.privacyfriendlydicer;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by yonjuni on 31.08.15.
 */
public class Preferences extends PreferenceActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();

    }

}
