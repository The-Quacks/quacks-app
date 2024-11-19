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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class CreateProfileTest {
    @Rule
    public ActivityScenarioRule<WelcomeEntrant> activityRule = new ActivityScenarioRule<>(WelcomeEntrant.class);

    @BeforeClass
    public static void setup() {
        // Set the system property to indicate to CRUD to use test db
        System.setProperty("junit.test", "true");
    }

    @AfterClass
    public static void tearDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CRUD.clearColection(User.class, new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                latch.countDown();
            }

            @Override
            public void onDeleteFailure(Exception e) {
                latch.countDown();
            }
        });

        // Block until async tasks are completed
        latch.await();
    }


    @Test
    public void testCreateProfile() {
        // wait for user data to be fetched from database
        sleep(1000);
        // Click on entrant home button
        onView(withId(R.id.welcome_home)).perform(click());

        sleep(1000);
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

        // Check if EntrantHome activity is displayed
        onView(withId(R.id.profileButton)).check(matches(isDisplayed()));
        sleep(1000);

        // Check that clicking profile button goes to profile activity
        onView(withId(R.id.profileButton)).perform(click());
        sleep(1000);
        onView(withId(R.id.profileHeaderText)).check(matches(isDisplayed()));
    }
}
