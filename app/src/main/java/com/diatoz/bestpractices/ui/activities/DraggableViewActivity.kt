package com.diatoz.bestpractices.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.diatoz.bestpractices.alarm.debug
import com.diatoz.bestpractices.databinding.ActivityDraggableViewBinding

class DraggableViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityDraggableViewBinding
    var lastAction: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDraggableViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.draggableView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                binding.draggableView.setOnTouchListener(onTouchListener)
                return true
            }
        })
    }

    private var onTouch = View.OnTouchListener { v, event ->
        debug("onTouch", "x: ${event?.x}")
        debug("onTouch", "y: ${event?.y}")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                debug("event", "ACTION_DOWN")
                lastAction = MotionEvent.ACTION_DOWN
            }
            MotionEvent.ACTION_MOVE -> {
                lastAction = MotionEvent.ACTION_MOVE
                debug("event", "ACTION_MOVE")
                v?.layoutParams?.width = event?.x.toInt()
                v?.layoutParams?.height = event?.y.toInt()
                v?.requestLayout()
            }
            MotionEvent.ACTION_UP -> {
                //binding.draggableView.setOnTouchListener(null)
                if (lastAction == MotionEvent.ACTION_MOVE) {
                    v?.layoutParams?.width = event?.x.toInt()
                    v?.layoutParams?.height = event?.y.toInt()
                    v?.requestLayout()
                }
            }
        }
        true
    }

    var dX = 0f
    var dY: Float = 0f

    private var onTouchListener = View.OnTouchListener { view, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            //MotionEvent.ACTION_UP -> binding.draggableView.setOnTouchListener(null)
            MotionEvent.ACTION_MOVE -> {
                val displayMetrics = DisplayMetrics()
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels

                debug("height", " : $height width $width")
                debug("dX", " :$dX dY $dY")
                if (dX < 150 && dY < 150)
                    view.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
            }
        }
        true
    }
}