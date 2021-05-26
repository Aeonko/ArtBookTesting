package com.nemanjamiseljic.artbooktesting.view

import android.app.ActivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.adapter.ImageRecyclerAdapter
import com.nemanjamiseljic.artbooktesting.databinding.FragmentImageApiBinding
import com.nemanjamiseljic.artbooktesting.util.Status
import com.nemanjamiseljic.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageApiFragment @Inject constructor(
     val imageRecyclerAdapter: ImageRecyclerAdapter
    ) : Fragment(R.layout.fragment_image_api) {


//    private val viewModel: ArtViewModel by activityViewModels()

    lateinit var viewModel: ArtViewModel

    private var binding: FragmentImageApiBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        binding = FragmentImageApiBinding.bind(view)

        subscribeToObservers()

        binding?.apply {
            var job: Job? = null
            searchText.addTextChangedListener {
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(1000)
                    it?.let {
                        if(it.toString().isNotEmpty()){
                            viewModel.searchForImage(it.toString())
                        }
                    }
                }
            }


            imageRecyclerView.adapter = imageRecyclerAdapter
            imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3)
            imageRecyclerAdapter.setOnItemClickListener {
                findNavController().popBackStack()
                viewModel.setSelectedImage(it)
            }
        }

    }

    private fun subscribeToObservers(){
        viewModel.imageList.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val urls: List<String>? = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }
                    if (urls != null){
                        imageRecyclerAdapter.images = urls
                    }


                    binding?.apply {
                        progressBar.visibility = View.GONE
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Error: ", Toast.LENGTH_SHORT).show()
                    binding?.apply {
                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    binding?.apply {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}