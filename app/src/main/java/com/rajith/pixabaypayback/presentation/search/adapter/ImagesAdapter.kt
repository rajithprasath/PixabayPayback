package com.rajith.pixabaypayback.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rajith.pixabaypayback.databinding.ImageItemBinding
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.presentation.common.adapter.TagsAdapter
import com.rajith.pixabaypayback.presentation.utils.IMAGE_VIEW_TYPE
import com.rajith.pixabaypayback.presentation.utils.NETWORK_VIEW_TYPE
import com.rajith.pixabaypayback.presentation.utils.imageDiffCallback

class ImagesAdapter(private val clicked: (Image, ImageView) -> Unit) :
    PagingDataAdapter<Image, ImagesAdapter.ImageViewHolder>(imageDiffCallback) {

    inner class ImageViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imagePassed: Image) {
            binding.apply {
                image = imagePassed
                val allTags = imagePassed.tags.split(',')
                rvTags.adapter = TagsAdapter(allTags as ArrayList<String>)
                root.setOnClickListener {
                    clicked.invoke(imagePassed, imageView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = getItem(position)!!
        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            NETWORK_VIEW_TYPE
        } else {
            IMAGE_VIEW_TYPE
        }
    }
}
