package com.chatkeeper.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One captured WhatsApp notification.
 * We save it the instant it arrives, so even if the sender later
 * deletes the message "for everyone", this copy stays untouched.
 */
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String,
    val message: String,
    val timestamp: Long,
    val packageName: String
)
