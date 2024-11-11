package com.westerra.release.activities

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.westerra.release.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class WesterraActivityTest {
    companion object {
        private val TAG = WesterraActivityTest::class.java.simpleName
    }

    @get:Rule
    var mActivityRule = ActivityScenarioRule(WesterraActivity::class.java)

    lateinit var activity: WesterraActivity

    @Before
    fun setUp() {
        mActivityRule.scenario.onActivity { onActivity ->
            activity = onActivity
        }
    }

    @Test
    fun invalidLoginTest() {
        splashScreenTest()
        handleNotificationsPopup()
        attemptLoginTest(username = "invalidname", password = "badpass")
        Log.i(TAG, "Detect toast")
        /*
        // Note: Toast detection doesn't seem to work on github action emulators but works in Android Studio
        onView(withText("Incorrect username or password. Please try again."))
            .check(matches(isDisplayed()))
        Log.i(TAG, "Invalid login test done")
         */
        onView(withId(R.id.authenticationJourney_usernameScreen_loginButton))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    private fun splashScreenTest() {
        Log.i(TAG, "Wait for splash progress")
        onView(withId(R.id.splashProgressBar))
            .inRoot(withDecorView(not(`is`(activity.window.decorView))))
        Log.i(TAG, "Splash progress detected, wait 3 seconds")
        Thread.sleep(3000)
        Log.i(TAG, "Splash screen test done")
    }

    private fun handleNotificationsPopup() {
        UiDevice.getInstance(getInstrumentation()).pressBack()
    }

    private fun attemptLoginTest(username: String, password: String) {
        Log.i(TAG, "Detect and click username field")
        onView(withId(R.id.authenticationJourney_usernameScreen_usernameField))
            .check(matches(isEnabled()))
            .perform(click())
        Thread.sleep(100)
        Log.i(TAG, "Enter Username")
        onView(withId(R.id.authenticationJourney_usernameScreen_usernameField))
            .check(matches(isEnabled()))
            .perform(typeText(username))
        Thread.sleep(500)
        hideKeyboard()
        Thread.sleep(1000)
        Log.i(TAG, "Detect and click password field")
        onView(withId(R.id.authenticationJourney_usernameScreen_passwordField))
            .check(matches(isEnabled()))
            .perform(click())
        Thread.sleep(100)
        Log.i(TAG, "Enter password")
        onView(withId(R.id.authenticationJourney_usernameScreen_passwordField))
            .check(matches(isEnabled()))
            .perform(typeText(password))
        Thread.sleep(500)
        hideKeyboard()
        Thread.sleep(1000)
        Log.i(TAG, "Detect and click login button")
        onView(withId(R.id.authenticationJourney_usernameScreen_loginButton))
            .check(matches(isEnabled()))
            .perform(click())
        Log.i(TAG, "Attempt login test done")
    }

    private fun hideKeyboard() {
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
    }

    @After
    fun tearDown() {
        // clean up code
    }
}
