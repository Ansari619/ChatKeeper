package com.chatkeeper.app.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.chatkeeper.app.data.AppDatabase
import com.chatkeeper.app.data.MessageEntity

/**
 * This service listens to notifications posted on the device.
 * When WhatsApp (or WhatsApp Business) posts a message notification,
 * we save the sender + text into our own local database right away.
 *
 * Important: we never touch WhatsApp itself. We only read notifications
 * that Android's system already shows to the user - this is a standard,
 * publicly documented Android API (NotificationListenerService), so it
 * does not put your WhatsApp account at any risk.
 *
 * Because the copy is saved the moment the notification appears, it stays
 * in our database even if the sender later deletes the message "for everyone".
 */
class ChatNotificationListener : NotificationListenerService() {

    private val watchedPackages = setOf("com.whatsapp", "com.whatsapp.w4b")

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        if (sbn.packageName !in watchedPackages) return

        val extras = sbn.notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: return
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: return

        // Skip WhatsApp's own summary/group notifications, we only want real messages
        if (title.isBlank() || text.isBlank()) return
        if (sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0) return

        val db = AppDatabase.getInstance(applicationContext)
        val entity = MessageEntity(
            sender = title,
            message = text,
            timestamp = sbn.postTime,
            packageName = sbn.packageName
        )

        // Use a simple background thread via a lifecycle-less coroutine scope substitute,
        // since NotificationListenerService is not a LifecycleService by default.
        Thread {
            kotlinx.coroutines.runBlocking {
                db.messageDao().insert(entity)
            }
        }.start()
    }
}
