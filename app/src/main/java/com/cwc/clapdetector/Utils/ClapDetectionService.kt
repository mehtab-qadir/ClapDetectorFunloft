package com.cwc.clapdetector.Utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.cwc.clapdetector.R
import com.cwc.clapdetector.UserInterface.MainActivity

class ClapDetectionService:Service() {
    private lateinit var clapDetector: ClapDetector

    override fun onCreate() {
        super.onCreate()
        clapDetector = ClapDetector(this, object : ClapDetector.ClapListener {
            override fun onEventDetected() {
                if(getsoundtimeposition() == 0){
                    if(getsoundactive()){
                        clapDetector.playSoundFromAssets(getsound()!!,0,true)
                    }
                }else{
                    if(getsoundactive()) {
                        clapDetector.playSoundFromAssets(getsound()!!, getsoundtimeposition(), false)
                    }
                }
                if(getvibrate()){
                    clapDetector.startVibrating(loadvibratePattern())
                }
                if(getflash()){
                    clapDetector.startFlashing(loadSOSPattern())
                }
            }
        })
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel(this)
        showNotification(this)
        clapDetector.startListening()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        clapDetector.stopListening()
        clapDetector.stopVibrating()
        clapDetector.stopFlashing(loadSOSPattern())
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // Create a notification channel (if needed)
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "your_channel_id"
            val channelName = "Clap Detector"
            val channelDescription = "Listening for claps..."

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Create and show a notification
    fun showNotification(context: Context) {
        // Create an intent for when the notification is clicked
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, "your_channel_id")
            .setContentTitle("Clap Detection")
            .setContentText("Listening for claps...")
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Adjust as needed
            .build()

        startForeground(1, notification)

    }
}