package org.secuso.privacyfriendlydicer.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.secuso.privacyfriendlydicer.R
import org.secuso.privacyfriendlydicer.databinding.DiceBinding
import java.util.Locale

class DiceAdapter(dices: IntArray, private val layoutInflater: LayoutInflater): RecyclerView.Adapter<DiceAdapter.ViewHolder>() {
    var dices: IntArray = dices
        set(value) {
            field = value
            @SuppressLint("NotifyDataSetChanged")
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DiceBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.dice.apply {
            if (dices[position] <= 6) {
                setBackgroundResource(when (dices[position]) {
                    1 -> R.drawable.d1
                    2 -> R.drawable.d2
                    3 -> R.drawable.d3
                    4 -> R.drawable.d4
                    5 -> R.drawable.d5
                    6 -> R.drawable.d6
                    else -> -1
                })
            } else {
                val height = 256;
                val width = 256;
                val textSize = width * 2.5f / 4.0f;

                val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(result).apply {
                    drawColor(ContextCompat.getColor(holder.binding.dice.context, R.color.colorAccent))
                }

                val p = Paint().apply {
                    color = Color.WHITE
                    typeface = Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                    this.textSize = textSize
                    isAntiAlias = true
                }
                canvas.drawText(String.format(Locale.ENGLISH, "%d", dices[position]), width / 2.0f, height / 2.0f + textSize / 3.0f, p);
                setBackgroundDrawable(BitmapDrawable(holder.binding.dice.context.resources, result))
            }

            val animation: Animation = AlphaAnimation(0.0f, 1.0f).apply {
                duration = 500
                startOffset = 20
                repeatMode = Animation.REVERSE
            }
            startAnimation(animation)
        }
    }

    override fun getItemCount() = dices.size


    class ViewHolder(val binding: DiceBinding): RecyclerView.ViewHolder(binding.root) {

    }
}