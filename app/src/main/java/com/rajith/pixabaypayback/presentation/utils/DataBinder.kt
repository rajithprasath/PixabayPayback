package com.rajith.pixabaypayback.presentation.utils

import android.graphics.Color
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Data binders to bind image to the views
 */
object DataBinder {
    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(imageView: ImageView, imageUrl: String?) {
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            val progress = CircularProgressDrawable(imageView.context).apply {
                strokeWidth = 5f
                centerRadius = 25f
            }
            progress.setColorSchemeColors(Color.BLUE)
            progress.start()
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(progress)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

}