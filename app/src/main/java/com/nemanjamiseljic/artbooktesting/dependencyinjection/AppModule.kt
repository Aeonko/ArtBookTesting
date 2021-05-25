package com.nemanjamiseljic.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.adapter.ImageRecyclerAdapter
import com.nemanjamiseljic.artbooktesting.api.RetrofitAPI
import com.nemanjamiseljic.artbooktesting.repo.ArtRepository
import com.nemanjamiseljic.artbooktesting.repo.ArtRepositoryInterface
import com.nemanjamiseljic.artbooktesting.roomdb.ArtDao
import com.nemanjamiseljic.artbooktesting.roomdb.ArtDatabase
import com.nemanjamiseljic.artbooktesting.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ArtDatabase::class.java, "ArtBookDb"
    ).build()


    @Singleton
    @Provides
    fun injectDao(database: ArtDatabase) = database.artDao()


    @Singleton
    @Provides
    fun injectRetrofitApi(): RetrofitAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) =
        Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
            )


    @Singleton
    @Provides
    fun injectNormalRepo(dao: ArtDao, api: RetrofitAPI) = ArtRepository(artDao = dao,retrofitAPI = api) as ArtRepositoryInterface


    @Singleton
    @Provides
    fun injectImageRecyclerAdapter(glide: RequestManager) = ImageRecyclerAdapter(glide = glide)


}