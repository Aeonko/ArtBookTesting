package com.nemanjamiseljic.artbooktesting.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.nemanjamiseljic.artbooktesting.roomdb.ArtDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.junit.runner.manipulation.Ordering
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("testDatabase")
    fun injectInMemoryRoomDB(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ArtDatabase::class.java).allowMainThreadQueries()
            .build()

}