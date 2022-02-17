package com.diatoz.bestpractices.alarm

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.diatoz.bestpractices.R
import com.diatoz.bestpractices.database.AppDatabase
import com.diatoz.bestpractices.database.ScheduleEntity
import kotlinx.android.synthetic.main.activity_add_time.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AddTimeActivity : AppCompatActivity(), CoroutineScope {

    val days = arrayListOf<String>()
    lateinit var job: Job

    var startTime: String? = null

    var startHour: Int? = 0
    var startMinute: Int? = 0
    private var endHour: Int? = 0
    var endMinute: Int? = 0

    private lateinit var myDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_time)

        job = Job()
        myDatabase = AppDatabase.invoke(this)

        saveTimerButton.setOnClickListener {
            when {
                startTime == null -> {
                    showToast(this, getString(R.string.please_select_time))
                }
                days.isEmpty() -> {
                    showToast(this, getString(R.string.please_select_days))
                }
                alarm_name.text.isEmpty() -> showToast(this, "Please enter schedule name")
                else -> {
                    addScheduleDay()
                }
            }
        }

        val calendar = Calendar.getInstance()
        startHour = calendar.get(Calendar.HOUR_OF_DAY)
        startMinute = calendar.get(Calendar.MINUTE)
        endHour = calendar.get(Calendar.HOUR_OF_DAY)
        endMinute = calendar.get(Calendar.MINUTE)

        end_time_layout.visibility = View.GONE

        add_start_time.setOnClickListener {
            selectTime("start")
        }

        add_end_time.setOnClickListener {
            selectTime("end")
        }

        set_start_time_schedule_tv.setOnClickListener {
            selectTime("start")
        }

        set_end_time_schedule_tv.setOnClickListener {
            selectTime("end")
        }

        backArrowScheduleTimer.setOnClickListener { onBackPressed() }

        day_sun.setOnCheckedChangeListener(dayCheckListener)
        day_mon.setOnCheckedChangeListener(dayCheckListener)
        day_tue.setOnCheckedChangeListener(dayCheckListener)
        day_wed.setOnCheckedChangeListener(dayCheckListener)
        day_thu.setOnCheckedChangeListener(dayCheckListener)
        day_fri.setOnCheckedChangeListener(dayCheckListener)
        day_sat.setOnCheckedChangeListener(dayCheckListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private var dayCheckListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            if (buttonView?.text.toString() == "T" && buttonView!!.id == day_tue.id) {
                days.add("Tu")
            } else if (buttonView?.text.toString() == "T" && buttonView!!.id == day_thu.id) {
                days.add("Th")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == day_sun.id) {
                days.add("Su")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == day_sat.id) {
                days.add("Sa")
            } else
                days.add(buttonView?.text.toString())
        } else {

            if (buttonView?.text.toString() == "T" && buttonView!!.id == day_tue.id) {
                days.remove("Tu")
            } else if (buttonView?.text.toString() == "T" && buttonView!!.id == day_thu.id) {
                days.remove("Th")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == day_sun.id) {
                days.remove("Su")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == day_sat.id) {
                days.remove("Sa")
            } else
                days.remove(buttonView?.text.toString())
        }
    }


    private fun addScheduleDay() {

        launch {
            val scheduleId = Random().nextInt()

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, startHour!!)
            calendar.set(Calendar.MINUTE, startMinute!!)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            var day = calendar.get(Calendar.DAY_OF_MONTH)

            /*check if the selected time has already passed, if yes then schedule the alarm for next day*/
            val calendarNow = Calendar.getInstance()
            if (calendarNow.timeInMillis >= calendar.timeInMillis) {
                day = calendar.get(Calendar.DAY_OF_MONTH) + 1
            }

            startHour = calendar.get(Calendar.HOUR_OF_DAY)
            startMinute = calendar.get(Calendar.MINUTE)

            setAlarm(
                scheduleId,
                startHour!!,
                startMinute!!,
                scheduleId,
                this@AddTimeActivity,
                day
            )

            val scheduleEntity = ScheduleEntity()

            scheduleEntity.scheduleStartHourTime = startHour!!.toInt()
            scheduleEntity.scheduleStartMinuteTime = startMinute!!.toInt()
            scheduleEntity.sunday = days.contains("Su")
            scheduleEntity.monday = days.contains("M")
            scheduleEntity.tuesday = days.contains("Tu")
            scheduleEntity.wednesday = days.contains("W")
            scheduleEntity.thusday = days.contains("Th")
            scheduleEntity.friday = days.contains("F")
            scheduleEntity.saturday = days.contains("Sa")
            scheduleEntity.id = scheduleId
            scheduleEntity.scheduleName = alarm_name.text.toString()

            myDatabase.scheduleDao().insertSchedule(scheduleEntity)

            val resultIntent = Intent()
            debug("id", "schedule id : $scheduleId")
            resultIntent.putExtra("scheduleId", scheduleId.toString())
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun selectTime(selection: String) {

        val alertDialog = Dialog(this)

        alertDialog.setContentView(R.layout.dialog_schedule_time_picker)
        val rbStart = alertDialog.findViewById<RadioButton>(R.id.rb_start)
        val rbCompletion = alertDialog.findViewById<RadioButton>(R.id.rb_completion)
        val btnCancel = alertDialog.findViewById<TextView>(R.id.btn_cancel)
        val btnOkay = alertDialog.findViewById<TextView>(R.id.btn_okay)
        val startTimePicker = alertDialog.findViewById<TimePicker>(R.id.start_time_picker)
        val endTimePicker = alertDialog.findViewById<TimePicker>(R.id.end_time_picker)

        btnCancel?.setOnClickListener { alertDialog.dismiss() }

        if (selection == "start") {
            rbStart?.isChecked = true
            startTimePicker.visibility = View.VISIBLE
            endTimePicker.visibility = View.GONE
        } else {
            rbCompletion?.isChecked = true
            startTimePicker.visibility = View.GONE
            endTimePicker.visibility = View.VISIBLE
        }

        //if (scheduleName.equals("sleep", true)) {
        rbCompletion.visibility = View.GONE
        //}

        startTimePicker.hour = startHour!!
        startTimePicker.minute = startMinute!!
        endTimePicker.hour = endHour!!
        endTimePicker.minute = endMinute!!

        rbCompletion?.setOnClickListener {
            startTime = startTimePicker.hour.toString() + ":" + startTimePicker.minute
            startTimePicker.visibility = View.GONE
            endTimePicker.visibility = View.VISIBLE
        }

        rbStart.setOnClickListener {
            startTimePicker.visibility = View.VISIBLE
            endTimePicker.visibility = View.GONE
        }

        btnOkay?.setOnClickListener {
            /*if (!scheduleName.equals("sleep", true)) {
                endTime = endTimePicker.hour.toString() + ":" + endTimePicker.minute
                endHour = endTimePicker.hour
                endMinute = endTimePicker.minute

                debug("endTime", "$endTime")
                set_end_time_schedule_tv.text =
                    get12HourFormat(endTimePicker.hour, endTimePicker.minute)
            }*/
            startTime = startTimePicker.hour.toString() + ":" + startTimePicker.minute
            startHour = startTimePicker.hour
            startMinute = startTimePicker.minute

            debug("startTime", "$startTime")
            set_start_time_schedule_tv.text =
                get12HourFormat(startTimePicker.hour, startTimePicker.minute)

            add_end_time.visibility = View.GONE
            add_start_time.visibility = View.GONE

            set_end_time_schedule_tv.visibility = View.VISIBLE
            set_end_time_iv.visibility = View.VISIBLE
            set_start_time_schedule_tv.visibility = View.VISIBLE
            set_start_time_iv.visibility = View.VISIBLE

            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}