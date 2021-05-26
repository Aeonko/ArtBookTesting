package com.nemanjamiseljic.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nemanjamiseljic.artbooktesting.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {


    //Rule that makes sure we run everything single threaded
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    // When not using HILT we can create Room Database here and use it this way
//    private lateinit var database: ArtDatabase
    private lateinit var dao: ArtDao

    @Before
    fun setup() {
        hiltRule.inject()
        // When not using HILT we can create Room Database here and use it this way
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ArtDatabase::class.java
//        ).allowMainThreadQueries().build()
//
        dao = database.artDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertArtTesting() = runBlockingTest{
        val exampleArt = Art("Mona Lisa","Da Vinci",1503,"test.com",1)
        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runBlockingTest{
        val exampleArt = Art("Mona Lisa","Da Vinci",1503,"test.com",1)
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValue()
        assertThat(list).doesNotContain(exampleArt)

    }

}