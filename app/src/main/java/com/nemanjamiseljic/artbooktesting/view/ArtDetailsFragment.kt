package com.nemanjamiseljic.artbooktesting.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.databinding.FragmentArtDetailsBinding
import com.nemanjamiseljic.artbooktesting.util.Status
import com.nemanjamiseljic.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
): Fragment(R.layout.fragment_art_details) {
    private val TAG by lazy { ArtDetailsFragment::class.java.name }

    private val viewModel : ArtViewModel by activityViewModels()
    private var binding: FragmentArtDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentArtDetailsBinding.bind(view)

        subscribeToObservers()

        binding?.apply {
            artImageView.setOnClickListener {
                findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
            }

        }

        val callback= object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        binding?.saveButton?.setOnClickListener{
            viewModel.makeArt(
                name = binding?.nameText?.text.toString(),
                artistName = binding?.artistText?.text.toString(),
                year = binding?.yearText?.text.toString()
            )
        }
    }

    private fun subscribeToObservers(){
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, { url ->
            Log.d(TAG, "subscribeToObservers: $url")
            binding?.let {
                glide.load(url).into(it.artImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}