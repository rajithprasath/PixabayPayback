package com.rajith.pixabaypayback.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.rajith.pixabaypayback.domain.model.Image

val imageDiffCallback = object : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}