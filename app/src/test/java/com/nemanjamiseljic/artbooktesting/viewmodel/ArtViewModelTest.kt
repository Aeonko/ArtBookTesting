package com.nemanjamiseljic.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nemanjamiseljic.MainCoroutineRule
import com.nemanjamiseljic.artbooktesting.getOrAwaitValueTest
import com.nemanjamiseljic.artbooktesting.repo.FakeArtRepository
import com.nemanjamiseljic.artbooktesting.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    //Rule that makes sure we run everything single threaded
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //Needed in only TEST folder and not in ANDROID TEST. Needed because we don't run this test's on emulator or physical devices
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel


    @Before
    fun setup(){
        //Test Doubles or testing fakes when functionality request fetching or posting data to server
        viewModel = ArtViewModel(FakeArtRepository())
    }

    @After
    fun onTearDown(){

    }

    @Test
    fun `insert art without year returns error`(){
        viewModel.makeArt("Mona Lisa","Da Vinci","")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert art without name returns error`(){
        viewModel.makeArt("","Da Vinci","1503")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert art without artistName returns error`(){
        viewModel.makeArt("Mona Lisa","","1503")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
}