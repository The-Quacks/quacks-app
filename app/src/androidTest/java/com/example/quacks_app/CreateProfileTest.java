package com.example.quacks_app;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateProfileTest {
    @Rule
    public ActivityScenarioRule<EntrantHome> activityRule = new ActivityScenarioRule<>(EntrantHome.class);

    @Test
    public void testCreateProfile() {
        // Click on profile button
        onView(withId(R.id.profileButton)).perform(click());

        // Check if CreateEntrantProfile activity is displayed
        onView(withId(R.id.profilePictureText)).check(matches(isDisplayed()));

        // Enter name, email, and phone number
        onView(withId(R.id.nameInput)).perform(typeText("John Duckworth"));
        onView(withId(R.id.emailInput)).perform(typeText("test@test.com"));
        onView(withId(R.id.editTextPhone)).perform(typeText("2963458213"));

        // Click on save button
        onView(withId(R.id.save)).perform(click());

        sleep(1000);
        // Check if EntrantHome activity is displayed
        onView(withId(R.id.profileButton)).check(matches(isDisplayed()));

        // Check that clicking profile button does nothing
        onView(withId(R.id.profileButton)).perform(click());
        onView(withId(R.id.welcomeTitleEntrant)).check(matches(isDisplayed()));
    }
}
