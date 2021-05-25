package com.nemanjamiseljic.artbooktesting.repo

import androidx.lifecycle.LiveData
import com.nemanjamiseljic.artbooktesting.model.ImageResponse
import com.nemanjamiseljic.artbooktesting.roomdb.Art
import com.nemanjamiseljic.artbooktesting.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>
}