package com.chatkeeper.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chatkeeper.app.data.MessageEntity
import com.chatkeeper.app.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var items: List<MessageEntity> = emptyList()
    private val timeFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    fun submitList(newItems: List<MessageEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MessageEntity) {
            binding.tvSender.text = item.sender
            binding.tvMessage.text = item.message
            binding.tvTime.text = timeFormat.format(Date(item.timestamp))
        }
    }
}
