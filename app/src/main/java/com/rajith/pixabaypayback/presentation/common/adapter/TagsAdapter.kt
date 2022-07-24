package com.rajith.pixabaypayback.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajith.pixabaypayback.databinding.ItemTagsBinding


class TagsAdapter(
    private var tagsList: ArrayList<String>
) : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.TagsViewHolder {
        return TagsViewHolder(
            ItemTagsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        val data = tagsList[position].trim()
        holder.bind(data)
    }

    inner class TagsViewHolder(private val binding: ItemTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(tag: String) {
            binding.apply {
                tvTag.text = "#$tag"
            }
        }
    }
}




