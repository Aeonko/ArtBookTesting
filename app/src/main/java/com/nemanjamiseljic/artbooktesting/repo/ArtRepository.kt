package com.nemanjamiseljic.artbooktesting.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.nemanjamiseljic.artbooktesting.api.RetrofitAPI
import com.nemanjamiseljic.artbooktesting.model.ImageResponse
import com.nemanjamiseljic.artbooktesting.roomdb.Art
import com.nemanjamiseljic.artbooktesting.roomdb.ArtDao
import com.nemanjamiseljic.artbooktesting.util.Resource
import java.lang.Exception
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI
) : ArtRepositoryInterface {

    private  val TAG by lazy { ArtRepository::class.java.name }
    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art = art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art = art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitAPI.imagesSearch(imageString)
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d(TAG, "searchImage: response success")
                    return@let Resource.success(it)
                } ?: Resource.error(msg = "Error", data = null)
            } else {
                Log.d(TAG, "searchImage: response error")
                Resource.error(msg = "Error", data = null)
            }
        } catch (e: Exception) {
            Log.d(TAG, "searchImage: response exception ${e.stackTrace}")
            Resource.error(msg = "No data!", data = null)
        }
    }
}