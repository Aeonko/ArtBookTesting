package com.nemanjamiseljic.artbooktesting.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemanjamiseljic.artbooktesting.model.ImageResponse
import com.nemanjamiseljic.artbooktesting.repo.ArtRepositoryInterface
import com.nemanjamiseljic.artbooktesting.roomdb.Art
import com.nemanjamiseljic.artbooktesting.util.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class ArtViewModel @ViewModelInject constructor(
    private val repository: ArtRepositoryInterface
) : ViewModel() {

    private val TAG by lazy { ArtViewModel::class.java.name }

    //Art Fragment

    val artList = repository.getArt()

    //Image API Fragment

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>
        get() = images


    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl: LiveData<String>
        get() = selectedImage


    // Art Details Fragment
    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage: LiveData<Resource<Art>>
        get() = insertArtMsg

    fun resetInsertArtMsg() {
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }


    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art = art)
    }

    fun makeArt(name: String, artistName: String, year: String) {
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) {
            insertArtMsg.postValue(Resource.error(msg = "Enter name, artist, year!", data = null))
            return
        }
        var yearInt = try {
            year.toInt()
        } catch (e: Exception) {
            insertArtMsg.postValue(Resource.error(msg = "Year should be number!", data = null))
            return
        }
        val art = Art(
            name = name,
            artistName = artistName,
            year = yearInt,
            imageUrl = selectedImage.value ?: ""
        )
        insertArt(art = art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(data = art))
    }


    fun searchForImage(searchString: String) {
        if (searchString.isEmpty()) {
            return
        }
        images.value = Resource.loading(data = null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
            response.data?.hits?.forEach {
                Log.d(TAG, "searchForImage: ${it.previewURL}")
            }
        }
    }
}