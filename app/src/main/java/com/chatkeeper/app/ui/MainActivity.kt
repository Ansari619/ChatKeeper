package com.chatkeeper.app.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.chatkeeper.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardDeletedMessages.setOnClickListener {
            startActivity(Intent(this, DeletedMessagesActivity::class.java))
        }

        binding.cardStatusSaver.setOnClickListener {
            startActivity(Intent(this, StatusSaverActivity::class.java))
        }

        // Notification access has to be granted manually by the user from
        // system settings - Android does not allow apps to enable it themselves.
        binding.btnEnableListener.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }
}
