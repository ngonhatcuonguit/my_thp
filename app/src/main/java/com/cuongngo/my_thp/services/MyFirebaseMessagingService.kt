package com.cuongngo.my_thp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.cuongngo.my_thp.App
import com.cuongngo.my_thp.R
import com.cuongngo.my_thp.data.local.AppPreferences
import com.cuongngo.my_thp.services.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance

class MyFirebaseMessagingService : FirebaseMessagingService(), KodeinAware {

    override val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Xử lý thông báo khi nhận được
        remoteMessage.notification?.let {
            val title = it.title ?: "Default Title"
            val body = it.body ?: "Default Body"
            showNotification(title, body)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Tạo Notification Channel (cho Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "fcm_default_channel", // ID kênh
                "Default Channel", // Tên kênh
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo thông báo
        val notificationBuilder = NotificationCompat.Builder(this, "fcm_default_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Icon thông báo
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Hiển thị thông báo
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // Xử lý khi FCM Token được cập nhật
        Log.d("FCM", "Refreshed token: $token")
        // Gửi token này lên server của bạn nếu cần
        updateFcmToken(token)
//        AppPreferences.setIsFcmToken(false)
    }

    private fun updateFcmToken(token: String?) {
        App.getInstance().fcmToken

        val userRepository: UserRepository = kodein.direct.instance()
        if (AppPreferences.getUserAccessToken().isNotEmpty() && token != null) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    userRepository.pushFcmToken(token)
                }
            }
        }else{
            AppPreferences.setIsFcmToken(false)
        }
    }

}


