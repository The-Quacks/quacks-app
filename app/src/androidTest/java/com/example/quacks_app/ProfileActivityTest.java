package com.example.quacks_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Before
    public void setUp() {
        // Initialize Espresso Intents
        Intents.init();
    }

    @After
    public void tearDown() {
        // Release Espresso Intents
        Intents.release();
    }

    /**
     * Test for opening the ProfileActivity and verifying its UI elements.
     */
    @Test
    public void testProfileActivityOpens() {
        // Create a test User instance
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ENTRANT);
        UserProfile userProfile = new UserProfile("Test User", "test@example.com", "1234567890");
        User testUser = new User("testDeviceId", roles, userProfile);

        // Launch ProfileActivity with the test User
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("User", testUser);

        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent)) {
            // Verify that ProfileActivity is launched
            onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
            onView(withId(R.id.userNameInput)).check(matches(isDisplayed()));
            onView(withId(R.id.emailInput)).check(matches(isDisplayed()));
            onView(withId(R.id.phoneNumberInput)).check(matches(isDisplayed()));
        }
    }

    /**
     * Test for removing the profile picture.
     */
    @Test
    public void testRemoveProfilePicture() {
        // Create a test User instance
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ENTRANT);
        UserProfile userProfile = new UserProfile("Test User", "test@example.com", "1234567890");
        User testUser = new User("testDeviceId", roles, userProfile);

        // Launch ProfileActivity with the test User
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("User", testUser);

        try (ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(intent)) {
            // Perform remove picture action
            onView(withId(R.id.removePictureButton)).perform(click());

            // Verify default profile picture is set
            onView(withId(R.id.profilePicture)).check(matches(isDisplayed()));
        }
    }
}
