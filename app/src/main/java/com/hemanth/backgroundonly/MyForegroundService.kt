package com.hemanth.backgroundonly

import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        val notification = NotificationCompat.Builder(this, "BG_CHANNEL")
            .setContentTitle("Service running")
            .setContentText("Background task active")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)

        // 🔹 Background thread
        handlerThread = HandlerThread("BG_THREAD")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        runnable = object : Runnable {
            override fun run() {
                Log.d("BG_SERVICE", "Service working at ${System.currentTimeMillis()}")
                handler.postDelayed(this, 15_000)
            }
        }

        handler.post(runnable)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        handlerThread.quitSafely()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "BG_CHANNEL",
                "Background Service",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.setSound(null, null) // 🔕 silent
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}