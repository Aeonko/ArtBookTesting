package com.nemanjamiseljic.artbooktesting.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.nemanjamiseljic.artbooktesting.R
import com.nemanjamiseljic.artbooktesting.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
class ArtFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }
    @After
    fun tearDown(){

    }

    @Test
    fun testNavigationFromArtToArtDetails(){
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtFragment>(
            factory = fragmentFactory
        ) {
            Navigation.setViewNavController(requireView(),navController)
        }

//        ViewActions.click()
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(click())


        Mockito.verify(navController).navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
    }
}