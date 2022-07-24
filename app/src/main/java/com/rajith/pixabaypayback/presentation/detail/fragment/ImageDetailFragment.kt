package com.rajith.pixabaypayback.presentation.detail.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.rajith.pixabaypayback.R
import com.rajith.pixabaypayback.databinding.FragmentImageDetailBinding
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.presentation.common.adapter.TagsAdapter
import com.rajith.pixabaypayback.presentation.utils.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailFragment : Fragment(R.layout.fragment_image_detail) {

    private lateinit var binding: FragmentImageDetailBinding
    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageDetailBinding.bind(view)
        binding.image = args.image
        setUpTagsAdapter(args.image)
        loadTransition(args.image)

        binding.toolbar.apply {
            setToolbar(this)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun setUpTagsAdapter(image: Image) {
        val allTags = image.tags.split(',')
        binding.rvTags.adapter = TagsAdapter(allTags as ArrayList<String>)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    private fun loadTransition(image: Image) {
        val imageUri = image.largeImageUrl
        binding.imageView.apply {
            transitionName = imageUri
            Glide.with(requireContext())
                .load(imageUri)
                .into(this)
        }
    }

}