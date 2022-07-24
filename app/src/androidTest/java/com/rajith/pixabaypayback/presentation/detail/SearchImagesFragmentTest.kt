package com.rajith.pixabaypayback.presentation.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rajith.pixabaypayback.R
import com.rajith.pixabaypayback.presentation.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchImagesFragmentTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun return_true_recyclerview_is_showing() {
        onView(withId(R.id.rvImages))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun return_true_search_view_is_showing() {
        onView(withId(R.id.etSearch))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Writes invalid search to query, waits for a moment then check if empty section is shown.
     */
    @Test
    fun return_true_empty_section_is_showing() {
        runBlocking {
            onView(withId(R.id.etSearch)).perform(replaceText("hahahsgdhshshshshhshs"))
                .perform(pressImeActionButton())
            delay(5000)
            onView(withId(R.id.empty_section)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

    }
}