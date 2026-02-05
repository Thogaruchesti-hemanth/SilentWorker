package com.hemanth.backgroundonly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class BootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BOOT_DEBUG", "BOOT_COMPLETED RECEIVED")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, MyForegroundService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}