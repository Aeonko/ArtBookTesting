package com.nemanjamiseljic.artbooktesting.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.nemanjamiseljic.artbooktesting.adapter.ArtRecyclerAdapter
import com.nemanjamiseljic.artbooktesting.adapter.ImageRecyclerAdapter
import javax.inject.Inject

class ArtFragmentFactory @Inject constructor(
    private val glide: RequestManager,
    private val artRecyclerAdapter: ArtRecyclerAdapter,
    private val imageRecyclerAdapter: ImageRecyclerAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            ArtFragment::class.java.name -> ArtFragment(artRecyclerAdapter = artRecyclerAdapter)
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide = glide)
            ImageApiFragment::class.java.name ->  ImageApiFragment(imageRecyclerAdapter = imageRecyclerAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}