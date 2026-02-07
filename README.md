# SilentWorker - Invisible Background Service for Android

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)

## 📋 Overview

**SilentWorker** is a lightweight, zero-UI Android application designed to run persistent background services without any visible interface or launcher icon. Perfect for developers building monitoring systems, automation tools, data sync services, or any background task that needs to run continuously without user interaction.

### Key Features

- ✨ **Completely Invisible** - No app icon in launcher, no UI whatsoever
- 🔄 **Auto-Start on Boot** - Automatically starts when device boots up
- 🛡️ **Persistent Service** - Runs as a foreground service with START_STICKY
- 🔇 **Silent Operation** - Low-priority, silent notifications
- 🧵 **Background Thread Processing** - Efficient HandlerThread implementation
- 📱 **Android 13+ Compatible** - Handles runtime notification permissions
- ⚡ **Battery Optimized** - Minimal resource consumption
- 🔒 **Crash Recovery** - Service restarts automatically if terminated

## 🎯 Use Cases

This application framework is ideal for:

- **IoT Device Management** - Monitor and control connected devices
- **Data Synchronization** - Periodic cloud sync services
- **Health Monitoring** - Background health metrics collection
- **Location Tracking** - GPS tracking for fleet management
- **Sensor Data Collection** - Continuous environmental monitoring
- **Automation Tasks** - Scheduled background operations
- **Network Monitoring** - Connection status tracking
- **File Watchers** - Monitor file system changes
- **Background Analytics** - Usage pattern collection
- **Remote Control Systems** - Listen for remote commands

## 📦 Installation

### Prerequisites

- Android Studio Arctic Fox or later
- Minimum SDK: Android 8.0 (API 26)
- Target SDK: Android 14 (API 34)
- Kotlin 1.9+

### Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/silentworker.git
cd silentworker
```

2. Open the project in Android Studio

3. Sync Gradle files

4. Build and install:
```bash
./gradlew installDebug
```

## 🏗️ Architecture

### Components

#### 1. **MyForegroundService**
The core service that runs continuously in the background.

- Runs as a foreground service with persistent notification
- Uses HandlerThread for background operations
- Executes tasks every 15 seconds (configurable)
- Implements START_STICKY for automatic restart

#### 2. **BootReceiver**
BroadcastReceiver that starts the service on device boot.

- Listens for `BOOT_COMPLETED` action
- Automatically launches the foreground service
- Ensures service persistence across reboots

#### 3. **HiddenSettingsActivity**
Minimal activity for permission handling (hidden from launcher).

- Requests notification permissions on Android 13+
- No visible UI component
- Only accessible via direct intent

### Service Lifecycle

```
Device Boot → BootReceiver → Start Service
                ↓
        MyForegroundService (Running)
                ↓
        Background Thread (HandlerThread)
                ↓
        Periodic Task Execution (15s interval)
                ↓
        Service Stopped → System Restarts (START_STICKY)
```

## ⚙️ Configuration

### Customising Task Interval

Edit `MyForegroundService.kt`:

```kotlin
// Change 15_000 (15 seconds) to your desired interval in milliseconds
handler.postDelayed(this, 15_000) // 15 seconds
handler.postDelayed(this, 60_000) // 1 minute
handler.postDelayed(this, 300_000) // 5 minutes
```

### Modifying Background Task

Replace the placeholder logic in `MyForegroundService.kt`:

```kotlin
runnable = object : Runnable {
    override fun run() {
        // 🔹 YOUR CUSTOM LOGIC HERE
        Log.d("BG_SERVICE", "Service working at ${System.currentTimeMillis()}")
        
        // Example: Network request, database operation, sensor reading, etc.
        
        handler.postDelayed(this, 15_000)
    }
}
```

### Notification Customization

Modify notification appearance in `MyForegroundService.onCreate()`:

```kotlin
val notification = NotificationCompat.Builder(this, "BG_CHANNEL")
    .setContentTitle("Your Service Title")
    .setContentText("Your description")
    .setSmallIcon(android.R.drawable.stat_notify_sync) // Change icon
    .setOngoing(true)
    .setSilent(true)
    .setPriority(NotificationCompat.PRIORITY_LOW)
    .build()
```

## 📱 Permissions

Required permissions in `AndroidManifest.xml`:

```xml
<!-- Start service on boot -->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

<!-- Foreground service (Android 9+) -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<!-- Notifications (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- Add additional permissions as needed -->
<!-- <uses-permission android:name="android.permission.INTERNET" /> -->
<!-- <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> -->
```

## 🚀 Usage

### Starting the Service

The service starts automatically on boot. To manually start:

```bash
# Using ADB
adb shell am start-foreground-service com.hemanth.backgroundonly/.MyForegroundService
```

### Stopping the Service

```bash
# Using ADB
adb shell am stopservice com.hemanth.backgroundonly/.MyForegroundService
```

### Accessing Hidden Settings

```bash
# Launch settings activity
adb shell am start -n com.hemanth.backgroundonly/.HiddenSettingsActivity
```

### Viewing Logs

```bash
# Filter service logs
adb logcat -s BG_SERVICE BOOT_DEBUG
```

## 🔧 Troubleshooting

### Service Not Starting on Boot

1. Check battery optimisation settings - disable for this app
2. Verify `RECEIVE_BOOT_COMPLETED` permission is granted
3. Some manufacturers (Xiaomi, Huawei) require autostart permission

### Service Gets Killed

1. Add App to the protected/whitelist apps in battery settings
2. Ensure the notification channel is not blocked
3. Consider implementing a sticky notification

### Notification Not Showing (Android 13+)

1. Notification permission must be granted
2. Launch `HiddenSettingsActivity` to request permission
3. User must manually approve in system settings

## 📊 Performance Considerations

- **Battery Usage**: Minimal - uses efficient HandlerThread
- **Memory Footprint**: < 10MB typical
- **CPU Usage**: Negligible during idle, configurable during tasks
- **Network**: Depends on your implementation

## 🔐 Security & Privacy

- No data collection by default
- No internet permissions unless you add them
- No analytics or tracking
- Completely open source

## 🛠️ Building for Production

### ProGuard Rules

Add to `proguard-rules.pro`:

```proguard
-keep class com.hemanth.backgroundonly.MyForegroundService { *; }
-keep class com.hemanth.backgroundonly.BootReceiver { *; }
```

### Release Build

```bash
./gradlew assembleRelease
```

## 📄 License
This project is licensed under the MIT License - see the  [LICENSE](LICENSE)  file for details.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/thogaruchesti-hemanth/silentworker/issues)
- **Discussions**: [GitHub Discussions](https://github.com/thogaruchesti-hemanth/silentworker/discussions)
- **Email**: saihemanth225@gmai.com

## ⚠️ Disclaimer

This application is designed for legitimate background processing use cases. Users are responsible for compliance with:

- Google Play Store policies
- Local privacy laws and regulations
- User consent requirements
- Battery optimisation guidelines

Misuse of background services may result in app removal from app stores or other consequences.

## 🙏 Acknowledgments

- Android Jetpack Components
- Kotlin Coroutines Community
- Android Developer Documentation

---

**Made with ❤️ for Android Developers**

*Star ⭐ this repository if you find it helpful!*
