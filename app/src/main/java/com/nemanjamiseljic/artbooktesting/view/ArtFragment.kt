package com.nemanjamiseljic.artbooktesting.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.adapter.ArtRecyclerAdapter
import com.nemanjamiseljic.artbooktesting.databinding.FragmentArtsBinding
import com.nemanjamiseljic.artbooktesting.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    val artRecyclerAdapter: ArtRecyclerAdapter
): Fragment(R.layout.fragment_arts) {
    private var binding: FragmentArtsBinding ? = null
    private lateinit var viewMode: ArtViewModel

    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition
            val selectedArt = artRecyclerAdapter.arts[layoutPosition]
            viewMode.deleteArt(selectedArt)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewMode = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)


        binding = FragmentArtsBinding.bind(view)
        subscribeToObservers()
        binding?.apply {
            recyclerViewArt.adapter = artRecyclerAdapter
            recyclerViewArt.layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(swipeCallBack).attachToRecyclerView(recyclerViewArt)
            fab.let {
               it.setOnClickListener {
                   findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
               }
            }
        }
    }

    private fun subscribeToObservers() {
        viewMode.artList.observe(viewLifecycleOwner,{
            if(it.isEmpty()){
                binding?.apply {
//                    recyclerViewArt.visibility = View.GONE
                    emptyListArtText.visibility = View.VISIBLE
                }

            }else{
                binding?.apply {
//                    recyclerViewArt.visibility = View.VISIBLE
                    emptyListArtText.visibility = View.GONE
                }
                artRecyclerAdapter.arts = it
            }

        })
    }
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}