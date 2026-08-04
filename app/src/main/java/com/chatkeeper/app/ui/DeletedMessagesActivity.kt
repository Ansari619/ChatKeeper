package com.chatkeeper.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatkeeper.app.data.AppDatabase
import com.chatkeeper.app.databinding.ActivityDeletedMessagesBinding

class DeletedMessagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeletedMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletedMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        val adapter = MessageAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val dao = AppDatabase.getInstance(applicationContext).messageDao()
        dao.getAll().observe(this) { messages ->
            adapter.submitList(messages)
            binding.tvEmpty.visibility = if (messages.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
