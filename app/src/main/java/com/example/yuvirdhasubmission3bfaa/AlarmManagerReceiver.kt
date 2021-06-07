package com.example.yuvirdhasubmission3bfaa

import com.example.yuvirdhasubmission3bfaa.activity.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import android.app.AlarmManager
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import java.util.*

class AlarmManagerReceiver : BroadcastReceiver() {

    companion object{
        private const val LOOPING_ID = 101
        private const val FORMAT = "HH:mm"

        const val TYPE = "Looping Alarm"
        const val EXTRA_REMINDER = "reminder"

        const val EXTRA_TYPE = "extra_type"
        const val TIME_RIGHT_NOW = "time_right_now"
    }

    // panggil metode ini saat menerima broadcast alarm receiver
    override fun onReceive(context: Context, intent: Intent) {

        // set title and id
        val tag = "Alarm Reminder at 9.00"
        val notification = LOOPING_ID

        // get string reminder
        val reminder = intent.getStringExtra(EXTRA_REMINDER)

        // get string extra time --> untuk percabangan settingan jam 9
        val rn = intent.getStringExtra(TIME_RIGHT_NOW)

        // get the time right now --> ambil waktu sesuai aturan HP
        val now = Calendar.getInstance().time

        // set to the default time
        val default = SimpleDateFormat(FORMAT, Locale.getDefault())
        val current: String  = default.format(now)

        // set time to 09.00am --> if alarm sets and the time is 09.00, the toast and the notification will show
        if(current == rn){if (reminder != null){
                alarmToast(context, tag, reminder)
                getNotification(context, reminder, notification)
            }
        }
    }

    //send to alarm notification
    private fun getNotification(context: Context, reminder: String, notifId: Int) {

        //set the channel
        val id = "My Channel 1"
        val tag = "REMIND ME AT 9 AM"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //set sound
        val setSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //set pending intent
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent, 0)

        //set builder
        val builder = NotificationCompat.Builder(context, id)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentIntent(pi)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(setSound)
                .setContentTitle("My Github Repeating Alarm")
                .setContentText(reminder)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setAutoCancel(true)

        //this is notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                    id,
                    tag,
                    NotificationManager.IMPORTANCE_DEFAULT)
            ch.enableVibration(true)
            ch.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(id)
            notificationManagerCompat.createNotificationChannel(ch)
        }

        //get notification
        val notif = builder.build()
        notificationManagerCompat.notify(notifId, notif)
    }

    //check if data is invalid
    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    //show toast if time is 09.00
    private fun alarmToast(context: Context, title: String, reminder: String?) {
        Toast.makeText(context, "$title : $reminder", Toast.LENGTH_LONG).show()
    }

    //cancelling alarm
    fun cancelAlarm(context: Context, type: String) {

        //validate input
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmManagerReceiver::class.java)

        //set pending intent
        val pi = PendingIntent.getBroadcast(context, LOOPING_ID, intent, 0)
        pi.cancel()
        am.cancel(pi)

        //set toast
        Toast.makeText(context, "Alarm is canceled.", Toast.LENGTH_SHORT).show()
    }

    //fungsi ini akan membuat alarm berulang setiap jam 9 pagi setelah di set
    fun getRepeatingReminder(context: Context, type: String, time: String, message: String){

        //check if data is invalid
        if (isDateInvalid(time, FORMAT)) return

        //set intent
        val intent = Intent(context, AlarmManagerReceiver::class.java)
        intent.putExtra(EXTRA_REMINDER, message)
        intent.putExtra(EXTRA_TYPE, type)
        intent.putExtra(TIME_RIGHT_NOW, time)

        val arrayOfTime = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        //set calendar
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY,
                Integer.parseInt(arrayOfTime[0]))
        cal.set(Calendar.MINUTE,
                Integer.parseInt(arrayOfTime[1]))
        cal.set(Calendar.SECOND,
                0)

        //set pending intent
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
                context,
                LOOPING_ID,
                intent,
                0
        )
        am.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                cal.timeInMillis,
                AlarmManager.INTERVAL_DAY, pi
        )

        //set the toast if alarm is set
        Toast.makeText(context, "Alarm is Set", Toast.LENGTH_SHORT).show()
    }
}