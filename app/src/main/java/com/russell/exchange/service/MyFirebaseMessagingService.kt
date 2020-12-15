package com.russell.exchange.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.russell.exchange.R
import com.russell.exchange.WebActivity
import org.json.JSONObject

/**
 * @author russell
 * @description:
 * @date : 2020/12/6 19:39
 */
class MyFirebaseMessagingService :FirebaseMessagingService(){

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    private  val TAG = "MyFirebaseMessagingServ"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: $remoteMessage")
        var pushMessage: PushMessage?=null
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            Log.d(TAG, "onMessageReceived: $it")
            if (it) {
                // Handle message within 10 seconds
                 pushMessage = Gson().fromJson(
                    remoteMessage.data["data"],
                    PushMessage::class.java
                )

            }
        }
        createNotification(baseContext, pushMessage)
    }

    private fun createNotification(context: Context, pushMessageModel: PushMessage?) {
        val channelId = getString(R.string.app_name)
        val notificationTitle: String
        notificationTitle = if (!pushMessageModel?.pushTopic.isNullOrBlank()) {
            pushMessageModel?.pushTopic!!
        } else {
            channelId
        }
        val builder = NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(notificationTitle)
            setContentText(pushMessageModel?.pushContent)
            setAutoCancel(true)
            val createTime = try {
                pushMessageModel?.createTime?.toLong() ?: System.currentTimeMillis()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
            setWhen(createTime)
            val brand = Build.BRAND
            val intent = setPendingIntent(context, pushMessageModel)
            setSmallIcon(R.drawable.icon_app)
            if (!TextUtils.isEmpty(brand) && brand.equals("samsung", ignoreCase = true)) {
                val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.icon_app)
                setLargeIcon(bitmap)
            }
            setContentIntent(intent)
            setDefaults(NotificationCompat.DEFAULT_ALL)
        }
        val notificationManager: NotificationManager? =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.apply {
            val notificationId = System.currentTimeMillis().toInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //android 8.0 消息通知渠道
                val notificationChannel =
                    createNotificationChannel(channelId, this)
            }
            this.notify(notificationId, builder.build())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        notificationManager: NotificationManager
    ): NotificationChannel {
        val notificationChannel =
            NotificationChannel(
                channelId,
                channelId,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true) //开启指示灯，如果设备有的话。
                enableVibration(true) //开启震动
                lightColor = Color.RED // 设置指示灯颜色
                lockscreenVisibility =
                    Notification.VISIBILITY_PRIVATE //设置是否应在锁定屏幕上显示此频道的通知
                setShowBadge(true) //设置是否显示角标
                setBypassDnd(true) // 设置绕过免打扰模式
                vibrationPattern = longArrayOf(100, 200, 300, 400) //设置震动频率
                description = channelId
            }
        notificationManager.createNotificationChannel(notificationChannel)
        return notificationChannel
    }


    private fun setPendingIntent(context: Context, data: PushMessage?): PendingIntent? {
        val intent: Intent?
        val url: String? = data?.url
        if (TextUtils.isEmpty(url)) {
            val packageManager = context.packageManager
            intent = packageManager.getLaunchIntentForPackage(context.packageName)
        } else {
            val jsonObject = JSONObject()
            intent=Intent(context, WebActivity::class.java)
            jsonObject.put("url", url)
            jsonObject.put("hasTitleBar", false)
            intent.putExtra("page", jsonObject.toString())
        }
        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }


    // [START on_new_token]
    // [END receive_message]
    // [START on_new_token]
    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
        val sharedPreferences=getSharedPreferences("data",Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString("token",token)
        editor.apply()
    }
    // [END on_new_token]

}

data class PushMessage(
    val url: String?,
    val pushTopic: String?,
    val pushContent: String?,
    val createTime: String?
)