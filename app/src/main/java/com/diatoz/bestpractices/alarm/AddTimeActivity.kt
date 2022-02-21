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
import com.diatoz.bestpractices.databinding.ActivityAddTimeBinding
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
    private lateinit var binding: ActivityAddTimeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTimeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        job = Job()
        myDatabase = AppDatabase.invoke(this)

        binding.saveTimerButton.setOnClickListener {
            when {
                startTime == null -> {
                    showToast(this, getString(R.string.please_select_time))
                }
                days.isEmpty() -> {
                    showToast(this, getString(R.string.please_select_days))
                }
                binding.alarmName.text.isEmpty() -> showToast(this, "Please enter schedule name")
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

        binding.endTimeLayout.visibility = View.GONE

        binding.addStartTime.setOnClickListener {
            selectTime("start")
        }

        binding.addEndTime.setOnClickListener {
            selectTime("end")
        }

        binding.setStartTimeScheduleTv.setOnClickListener {
            selectTime("start")
        }

        binding.setEndTimeScheduleTv.setOnClickListener {
            selectTime("end")
        }

        binding.backArrowScheduleTimer.setOnClickListener { onBackPressed() }

        binding.daySun.setOnCheckedChangeListener(dayCheckListener)
        binding.dayMon.setOnCheckedChangeListener(dayCheckListener)
        binding.dayTue.setOnCheckedChangeListener(dayCheckListener)
        binding.dayWed.setOnCheckedChangeListener(dayCheckListener)
        binding.dayThu.setOnCheckedChangeListener(dayCheckListener)
        binding.dayFri.setOnCheckedChangeListener(dayCheckListener)
        binding.daySat.setOnCheckedChangeListener(dayCheckListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private var dayCheckListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {
            if (buttonView?.text.toString() == "T" && buttonView!!.id == binding.dayTue.id) {
                days.add("Tu")
            } else if (buttonView?.text.toString() == "T" && buttonView!!.id == binding.dayThu.id) {
                days.add("Th")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == binding.daySun.id) {
                days.add("Su")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == binding.daySat.id) {
                days.add("Sa")
            } else
                days.add(buttonView?.text.toString())
        } else {

            if (buttonView?.text.toString() == "T" && buttonView!!.id == binding.dayTue.id) {
                days.remove("Tu")
            } else if (buttonView?.text.toString() == "T" && buttonView!!.id == binding.dayThu.id) {
                days.remove("Th")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == binding.daySun.id) {
                days.remove("Su")
            } else if (buttonView?.text.toString() == "S" && buttonView!!.id == binding.daySat.id) {
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
            scheduleEntity.scheduleName = binding.alarmName.text.toString()

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

        rbCompletion.visibility = View.GONE

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
            startTime = startTimePicker.hour.toString() + ":" + startTimePicker.minute
            startHour = startTimePicker.hour
            startMinute = startTimePicker.minute

            debug("startTime", "$startTime")
            binding.setStartTimeScheduleTv.text =
                get12HourFormat(startTimePicker.hour, startTimePicker.minute)

            binding.addEndTime.visibility = View.GONE
            binding.addStartTime.visibility = View.GONE

            binding.setEndTimeScheduleTv.visibility = View.VISIBLE
            binding.setEndTimeIv.visibility = View.VISIBLE
            binding.setStartTimeScheduleTv.visibility = View.VISIBLE
            binding.setStartTimeIv.visibility = View.VISIBLE

            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
}