package com.mikhaellopez.circularprogressbarsample

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.larswerkman.lobsterpicker.OnColorListener
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    val maxProgress = 100f
    var currentProgress = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Init progress with animation
        //circularProgressBar.setProgressWithAnimation(65f, 1000) // =1s



        // Update circularProgressBar
        seekBarProgress.onProgressChanged { circularProgressBar.progress = it }
        seekBarStartAngle.onProgressChanged { circularProgressBar.startAngle = it }
        seekBarStrokeWidth.onProgressChanged { circularProgressBar.progressBarWidth = it }
        seekBarBackgroundStrokeWidth.onProgressChanged { circularProgressBar.backgroundProgressBarWidth = it }
        shadeSlider.onColorChanged {
            circularProgressBar.progressBarColor = it
            circularProgressBar.backgroundProgressBarColor = adjustAlpha(it, 0.3f)
        }
        switchRoundBorder.onCheckedChange { circularProgressBar.roundBorder = it }
        switchProgressDirection.onCheckedChange {
            circularProgressBar.progressDirection =
                    if (it) CircularProgressBar.ProgressDirection.TO_RIGHT
                    else CircularProgressBar.ProgressDirection.TO_LEFT
        }
        testAnimationEndListenerButton.setOnClickListener {
            circularProgressBar.setProgressWithAnimation(maxProgress, 1000,
                    onAnimationEnd = {
                        Log.d("Test", "onAnimationEnd maxProgress reached")
                        Toast.makeText(this, "100% reached", Toast.LENGTH_SHORT).show()
                    })
        }

        // Indeterminate Mode
        switchIndeterminateMode.onCheckedChange { circularProgressBar.indeterminateMode = it }
        circularProgressBar.onIndeterminateModeChangeListener = { switchIndeterminateMode.isChecked = it }

        circularProgressBar.progressMax = maxProgress


        //circularProgressBar.onProgressChangeListener = { progress ->
        //    if (progress == maxProgress) {
        //        Log.d("Test", "onProgressChangeListener maxProgress reached")
        //    }
        //}
        updateProgress()
    }

    fun updateProgress() {
        currentProgress = currentProgress + 10
        if (currentProgress > maxProgress) {
            currentProgress = 0f
        }

        if (currentProgress == maxProgress) {
            circularProgressBar.setProgressWithAnimation(maxProgress, 200, onAnimationEnd =
            {
                //circularProgressBar.progress = 0f
            })
            circularProgressBar.onProgressChangeListener = { progress ->
                if (progress == maxProgress) {
                    circularProgressBar.progress = 0f
                    Log.d("Test", "onProgressChangeListener maxProgress reached")
                }
            }
            //circularProgressBar.setProgressWithAnimation(maxProgress, 200)
            //Handler().postDelayed({circularProgressBar.progress = 0f}, 220)
        } else {
            circularProgressBar.setProgressWithAnimation(currentProgress, 250)
        }
        Handler().postDelayed({updateProgress()}, 250)
    }

    //region Extensions
    private fun SeekBar.onProgressChanged(onProgressChanged: (Float) -> Unit) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Nothing
            }
        })
    }

    private fun LobsterShadeSlider.onColorChanged(onColorChanged: (Int) -> Unit) {
        addOnColorListener(object : OnColorListener {
            override fun onColorChanged(color: Int) {
                onColorChanged(color)
            }

            override fun onColorSelected(color: Int) {
                // Nothing
            }
        })
    }

    private fun SwitchCompat.onCheckedChange(onCheckedChange: (Boolean) -> Unit) {
        setOnCheckedChangeListener { _, isChecked -> onCheckedChange(isChecked) }
    }
    //endregion

    /**
     * Transparent the given progressBarColor by the factor
     * The more the factor closer to zero the more the progressBarColor gets transparent
     *
     * @param color  The progressBarColor to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted progressBarColor
     */
    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

}