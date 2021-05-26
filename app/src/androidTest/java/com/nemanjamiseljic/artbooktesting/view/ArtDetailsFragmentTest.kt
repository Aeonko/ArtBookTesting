package com.nemanjamiseljic.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.getOrAwaitValue
import com.nemanjamiseljic.artbooktesting.launchFragmentInHiltContainer
import com.nemanjamiseljic.artbooktesting.repo.FakeArtRepositoryAndroidTest
import com.nemanjamiseljic.artbooktesting.roomdb.Art
import com.nemanjamiseljic.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    //Rule that makes sure we run everything single threaded
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {

    }


    @Test
    fun testNavigationFromArtDetailsFragmentToImageApi() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }
//        ViewActions.click()
        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())

        Mockito.verify(navController)
            .navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment())
    }


    @Test
    fun testOnBackPress() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave() {
        val navController = Mockito.mock(NavController::class.java)
        val testViewModel = ArtViewModel(FakeArtRepositoryAndroidTest())
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
//            viewModel is property in not Fake ArtDetailsFragment
            viewModel = testViewModel
        }

        Espresso.onView(withId(R.id.nameText)).perform(replaceText("Mona Lisa"))
        Espresso.onView(withId(R.id.artistText)).perform(replaceText("Da Vinci"))
        Espresso.onView(withId(R.id.yearText)).perform(replaceText("1503"))
        Espresso.onView(withId(R.id.saveButton)).perform(click())

        assertThat(testViewModel.artList.getOrAwaitValue()).contains(
            Art(
                name = "Mona Lisa",
                artistName = "Da Vinci",
                year = 1503,
                imageUrl = ""
            )
        )

    }
}