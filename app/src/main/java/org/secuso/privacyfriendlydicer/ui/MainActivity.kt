package org.secuso.privacyfriendlydicer.ui

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import org.secuso.privacyfriendlydicer.checkGoodbyeGoogle
import org.secuso.privacyfriendlydicer.R
import org.secuso.privacyfriendlydicer.databinding.ActivityMainBinding
import org.secuso.privacyfriendlydicer.databinding.ContentMainBinding
import org.secuso.privacyfriendlydicer.sensors.ShakeListener
import org.secuso.privacyfriendlydicer.sensors.ShakeListener.OnShakeListener
import java.util.Locale

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val dicerViewModel by lazy { ViewModelProvider(this).get<DicerViewModel>(DicerViewModel::class.java) }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val contentMainBinding by lazy { ContentMainBinding.bind(binding.root) }

    private val adapter: DiceAdapter by lazy { DiceAdapter(dicerViewModel.dicerLiveData.value ?: IntArray(0), layoutInflater) }
    private var shakingEnabled = false
    private var vibrationEnabled = false
    private val sharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    // for Shaking
    private val sensorManager: SensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val accelerometer: Sensor? by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private val shakeListener = ShakeListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        contentMainBinding.dices.adapter = adapter
        contentMainBinding.dices.layoutManager = object : GridLayoutManager(this, 5, VERTICAL, false) {
            override fun canScrollHorizontally() = false
            override fun canScrollVertically() = false
        }

        initResources()

        dicerViewModel.dicerLiveData.observe(this, object : Observer<IntArray> {
            override fun onChanged(dice: IntArray) {
                adapter.dices = dice
                displaySum()

                if (vibrationEnabled) {
                    val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(50)
                }
            }
        })

        dicerViewModel.diceNumberLiveData.observe(this, object : Observer<Int> {
            override fun onChanged(number: Int) {
                contentMainBinding.chooseDiceNumber.text = String.format(Locale.ENGLISH, "%d", number)
            }
        })

        dicerViewModel.faceNumberLiveData.observe(this, object : Observer<Int> {
            override fun onChanged(number: Int) {
                contentMainBinding.chooseFaceNumber.text = String.format(Locale.ENGLISH, "%d", number)
            }
        })
        checkGoodbyeGoogle(this, layoutInflater)
    }

    private fun initResources() {
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        contentMainBinding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dicerViewModel.setDiceNumber(progress + 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        contentMainBinding.seekBarFace.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dicerViewModel.setFaceNumber(progress + 1)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        //Button
        contentMainBinding.rollButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                rollDice()
            }
        })

        //Shaking
        shakeListener.setOnShakeListener(object : OnShakeListener {
            override fun onShake(count: Int) {
                if (shakingEnabled) {
                    rollDice()
                }
            }
        })
    }

    fun rollDice() {
        applySettings()
        dicerViewModel.rollDice()
    }

    private fun displaySum() {
        contentMainBinding.sumTextView.text = getString(R.string.main_dice_sum, adapter.dices.sum().toString())
    }

    fun applySettings() {
        shakingEnabled = sharedPreferences.getBoolean("enable_shaking", true)
        vibrationEnabled = sharedPreferences.getBoolean("enable_vibration", true)
    }

    public override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            shakeListener, accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )

        applySettings()
    }

    public override fun onPause() {
        sensorManager.unregisterListener(shakeListener)
        super.onPause()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    /**
     * Handle navigation view item clicks here.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val intent = when (item.itemId) {
            R.id.nav_about -> Intent(this, AboutActivity::class.java)
            R.id.nav_help -> Intent(this, HelpActivity::class.java)
            R.id.nav_settimgs -> Intent(this, SettingsActivity::class.java)
            R.id.nav_tutorial -> Intent(this, TutorialActivity::class.java)
            else -> return false
        }

        startActivity(intent)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
