package com.example.quacks_app;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JoinWaitlistTest {
    @Rule
    public ActivityScenarioRule<EntrantHome> activityRule = new ActivityScenarioRule<>(EntrantHome.class);

    // This instrumented test creates a new user profile, "scans" an event QR code, and then joins
    // the waitlist for that event. Actually scanning/using a QR code in a test is not easily done,
    // so the same information is instead hardcoded here and passed to the even activity.
    @Test
    public void testJoinWaitlist() {
        // Click on profile button
        onView(withId(R.id.profileButton)).perform(click());

        // Check if CreateEntrantProfile activity is displayed
        onView(withId(R.id.profilePictureText)).check(matches(isDisplayed()));

        // Enter name, email, and phone number
        onView(withId(R.id.nameInput)).perform(typeText("John Duckworth"));
        onView(withId(R.id.emailInput)).perform(typeText("test@test.com"));
        onView(withId(R.id.editTextPhone)).perform(typeText("2963458213"));

        // Collapse keyboard
        onView(withId(R.id.editTextPhone)).perform(closeSoftKeyboard());

        // Click on save button
        onView(withId(R.id.save)).perform(click());

        sleep(1000); // Non-ideal way to wait for previous action to finish
        // Check if EntrantHome activity is displayed
        onView(withId(R.id.profileButton)).check(matches(isDisplayed()));

        // Check that clicking profile button does nothing (temporary, until profile functionality
        // is implemented)
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.welcomeTitleEntrant)).check(matches(isDisplayed()));

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent switchActivityIntent = new Intent(context, EventDescription.class);
        switchActivityIntent.putExtra("id", "IxP9RUJUcm9UF7trq635");

        try (ActivityScenario<EventDescription> scenario = ActivityScenario.launch(switchActivityIntent)) {
            onView(withId(R.id.eventTitle)).check(matches(isDisplayed()));
            onView(withId(R.id.joinWaitlistButton)).perform(click());
            scenario.close();
        }

        onView(withId(R.id.welcomeTitleEntrant)).check(matches(isDisplayed()));
    }
}
