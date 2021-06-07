package com.example.yuvirdhasubmission3bfaa.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.yuvirdhasubmission3bfaa.AlarmManagerReceiver
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var receiver: AlarmManagerReceiver
    private lateinit var binding: ActivityNotificationBinding

    companion object {
        //set the time --> 09.00
        private const val SET_TIME = "09:00"

        //set the toast --> if alarm is set at 09.00, the toast will show
        private const val REMINDER = "CHECK YOUR GITHUB RIGHT NOW!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        //set binding
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set receiver
        receiver = AlarmManagerReceiver()

        //set alarm button
        binding.alarmBtn.setOnClickListener(this)

        //set cancel button
        binding.cancelBtn.setOnClickListener(this)

        // showing the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set title bar
        supportActionBar?.setTitle(R.string.alarm_page)
    }

    //set button back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //set onclick button
    override fun onClick(view: View) {
        if(view.id == R.id.alarm_btn){
            receiver.getRepeatingReminder(view.context, AlarmManagerReceiver.TYPE, SET_TIME, REMINDER)
        }
        else if(view.id == R.id.cancel_btn){
            receiver.cancelAlarm(this, AlarmManagerReceiver.TYPE)
        }
    }
}