package com.diatoz.bestpractices.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.diatoz.bestpractices.alarm.debug

open class DraggableView(context: Context, attrs: AttributeSet) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    var dx: Float = 0F
    var dy: Float = 0F
    var lastAction: Int? = 0


    /*override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                dx = x - event.rawX
                dy = y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
            }
            MotionEvent.ACTION_UP -> {
                dx = x - event.rawX
                dy = y - event.rawY
                lastAction = MotionEvent.ACTION_UP
            }
            MotionEvent.ACTION_MOVE -> {
                dx = x - event.rawX
                dy = y - event.rawY
                lastAction = MotionEvent.ACTION_MOVE
            }
            else -> {
                return false
            }
        }
        return true

    }*/

    override fun onDragEvent(event: DragEvent?): Boolean {
        debug("onDragEvent", ": ${event?.action}")
        when (event?.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                invalidate()
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                invalidate()
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                invalidate()
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                invalidate()
            }

        }
        return false
    }
}