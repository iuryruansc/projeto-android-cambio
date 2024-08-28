package com.currencyview

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.currencyview.common.idling.ApiIdlingResource
import com.currencyview.ui.views.activities.MainActivity
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyConverterTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(ApiIdlingResource.getIdlingResource())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(ApiIdlingResource.getIdlingResource())
    }

    @Test
    fun testCurrencyConversion() {
        onView(withId(R.id.currency_selection_input_container)).perform(click())

        onData(instanceOf(String::class.java))
            .onChildView(withId(R.id.currency_item_text)).atPosition(1).perform(click())

    }
}
