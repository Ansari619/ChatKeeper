package com.chatkeeper.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chatkeeper.app.databinding.ItemStatusBinding

class StatusAdapter(
    private val onSaveClick: (StatusItem) -> Unit
) : RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    private var items: List<StatusItem> = emptyList()

    fun submitList(newItems: List<StatusItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = ItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class StatusViewHolder(private val binding: ItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StatusItem) {
            Glide.with(binding.ivThumb.context)
                .load(item.uri)
                .centerCrop()
                .into(binding.ivThumb)

            binding.btnSave.setOnClickListener { onSaveClick(item) }
        }
    }
}
