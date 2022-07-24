package com.rajith.pixabaypayback.presentation.search.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.rajith.pixabaypayback.R
import com.rajith.pixabaypayback.databinding.FragmentSearchImagesBinding
import com.rajith.pixabaypayback.domain.model.Image
import com.rajith.pixabaypayback.presentation.search.adapter.ImagesAdapter
import com.rajith.pixabaypayback.presentation.search.adapter.LoadingStateAdapter
import com.rajith.pixabaypayback.presentation.search.viewmodel.SearchViewModel
import com.rajith.pixabaypayback.presentation.utils.IMAGE_VIEW_TYPE
import com.rajith.pixabaypayback.presentation.utils.ItemOffsetDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchImagesFragment : Fragment(R.layout.fragment_search_images) {
    private lateinit var binding: FragmentSearchImagesBinding
    private val adapter = ImagesAdapter { image, imageView -> navigate(image, imageView) }
    private val viewModel: SearchViewModel by activityViewModels()
    private var gridLayoutSpan = 2
    private var isInitiated = false
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchImagesBinding.bind(view)
        setSearchViewListener()
        setUpAdapter()

        if (!isInitiated) init()

        binding.btnRetry.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchImages(query, true)
            }
        }

    }

    private fun setUpAdapter() {
        val itemDecoration =
            ItemOffsetDecoration(requireContext(), R.dimen.size_3)
        binding.rvImages.addItemDecoration(itemDecoration)


        val currentOrientation = resources.configuration.orientation
        gridLayoutSpan = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), gridLayoutSpan)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == IMAGE_VIEW_TYPE) 1
                else gridLayoutSpan
            }
        }
        binding.rvImages.layoutManager = gridLayoutManager
        binding.rvImages.adapter = adapter
        binding.rvImages.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { state ->
            binding.progress.isVisible = state.refresh is LoadState.Loading
            binding.emptySection.isVisible =
                state.refresh is LoadState.NotLoading && adapter.itemCount == 0
            binding.errorSection.isVisible = state.refresh is LoadState.Error && adapter.itemCount == 0

        }
    }

    private fun init() {
        isInitiated = true
        searchImages(viewModel.currentSearch)
        binding.etSearch.apply {
            setText(viewModel.currentSearch)
            text?.length?.let { setSelection(it) }
        }
    }

    private fun searchImages(searchString: String, isUserInitiated: Boolean = false) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            if (isUserInitiated) adapter.submitData(PagingData.empty())
            viewModel.searchImages(searchString).collectLatest {
                adapter.submitData(it)
                hideSoftKeyboard()
            }
        }
    }

    private fun setSearchViewListener() {
        binding.etSearch.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = text.toString().trim()
                    if (query.isNotEmpty()) {
                        searchImages(query, true)
                    } else {
                        binding.etSearch.text = null
                    }
                    hideSoftKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }

    private fun navigate(image: Image, imageView: ImageView) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setMessage(getString(R.string.text_see_details))
        alertDialog.setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
            dialog.dismiss()
            val extras = FragmentNavigatorExtras(
                imageView to image.largeImageUrl
            )
            val action = SearchImagesFragmentDirections.toImageDetailFragment(image)
            findNavController().navigate(action, extras)
        }
        alertDialog.setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()

    }

    private fun retry() {
        adapter.retry()
    }

    private fun hideSoftKeyboard() {
        requireActivity().apply {
            WindowInsetsControllerCompat(
                window,
                window.decorView
            ).hide(WindowInsetsCompat.Type.ime())
        }
    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }
}