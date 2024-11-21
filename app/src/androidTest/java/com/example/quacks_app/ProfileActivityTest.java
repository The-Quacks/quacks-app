package com.example.quacks_app;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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

    @Test
    public void testProfileActivityOpens() {
        // Launch the AdminHome activity
        ActivityScenario<AdminHome> scenario = ActivityScenario.launch(AdminHome.class);

        // Click on the Profile button
        onView(withId(R.id.profilesButton)).perform(click());

        // Verify that ProfileActivity is launched
        Intents.intended(IntentMatchers.hasComponent(AdminListView.class.getName()));

        // Check if a view from ProfileActivity is displayed
        onView(withId(R.id.listViewType)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Close the scenario
        scenario.close();
    }
}
