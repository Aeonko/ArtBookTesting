package com.nemanjamiseljic.artbooktesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nemanjamiseljic.artbooktesting.model.ImageResponse
import com.nemanjamiseljic.artbooktesting.roomdb.Art
import com.nemanjamiseljic.artbooktesting.util.Resource

class FakeArtRepository: ArtRepositoryInterface{
    /**Mainly created to test how viewModels work because we need this methods from repository because viewModels update Gui depending on actions happening in repository
     * Fake repository just needs to simulate working of real repository and not ask for dependencies like real one**/

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(),0,0))
    }

    private fun refreshData(){
        artsLiveData.postValue(arts)
    }
}