package com.example.quacks_app;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.anything;

import static org.hamcrest.CoreMatchers.not;

import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(AndroidJUnit4.class)
public class OrganizerHomeTest {

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

    /**
     * Tests update/creating an organizer profile
     */

    @Test
    public void testProfile(){
        // wait for user data to be fetched from database
        String testId = UUID.randomUUID().toString();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean exists = new AtomicBoolean(false);


        CRUD.readStatic(testId, User.class, new ReadCallback<User>() {
            @Override
            public void onReadSuccess(User user) {
                if (user != null ){
                    //user exists so they already have a profile-- we will update it in this section
                    onView(withId(R.id.welcome_home)).perform(click());

                    //getting to the edit page
                    onView(withId(R.id.profile_spinner_entrant)).perform(click());
                    onData(anything()).atPosition(1).perform(click());

                    onView(withId(R.id.organizer_profile_button)).check(matches(isDisplayed()));
                    onView(withId(R.id.organizer_profile_button)).perform(click());
                    onView(withId(R.id.edit_button)).check(matches(isDisplayed()));
                    onView(withId(R.id.edit_button)).perform(click());

                    // typing in new info
                    onView(withId(R.id.edit_title)).perform(typeText("Boston Pizza"));
                    onView(withId(R.id.edit_location)).perform(typeText("21 Briarwood Point, Stony Plain, Alberta"));
                    onView(withId(R.id.edit_contact_info)).perform(typeText("780-123-2345"));
                    onView(withId(R.id.edit_facility_details)).perform(typeText("Birthday Party for Greg"));
                    onView(withId(R.id.edit_email)).perform(typeText("abc@gmail.com"));
                    onView(withId(R.id.edit_username)).perform(typeText("BOSTONPIZZA"));
                    onView(withId(R.id.edit_password)).perform(typeText("CheesePizza1999!"));

                    //checking
                    onView(withId(R.id.edit_confirm_button)).perform(click());

                    //if it is updated it will go to the organizer homepage
                    onView(withId(R.id.organizer_profile_button)).check(matches(isDisplayed()));

                }
            }

            @Override
            public void onReadFailure(Exception e) {

                //User does not exist and a new facility profile is made
                onView(withId(R.id.CREATE_FACILITY)).perform(click());
                onView(withId(R.id.create_title)).check(matches(isDisplayed()));

                // Type
                onView(withId(R.id.create_title)).perform(typeText("Boston Pizza"));
                onView(withId(R.id.create_location)).perform(typeText("21 Briarwood Point, Stony Plain, Alberta"));
                onView(withId(R.id.contact_info)).perform(typeText("780-123-2345"));
                onView(withId(R.id.create_facility_details)).perform(typeText("Birthday Party for Greg"));
                onView(withId(R.id.create_accessibility)).perform(typeText("Yes with ramps"));
                onView(withId(R.id.create_email)).perform(typeText("abc@gmail.com"));
                onView(withId(R.id.create_username)).perform(typeText("BOSTONPIZZA"));
                onView(withId(R.id.create_password)).perform(typeText("CheesePizza1999!"));

                //check
                onView(withId(R.id.create_confirm_button)).perform(click());

                //if its created it will go to homepage
                onView(withId(R.id.organizer_profile_button)).check(matches(isDisplayed()));

            }
        });
    }

    /**
     * Tests whether we can create an event
     */

    @Test
    public void CreateEvent(){

        String testId = UUID.randomUUID().toString();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean exists = new AtomicBoolean(false);


        CRUD.readStatic(testId, User.class, new ReadCallback<User>() {
            @Override
            public void onReadSuccess(User data) {

                if (data != null){
                    onView(withId(R.id.welcome_home)).perform(click());

                    //getting to the edit page
                    onView(withId(R.id.profile_spinner_entrant)).perform(click());
                    onData(anything()).atPosition(1).perform(click());
                    onView(withId(R.id.organizer_profile_button)).check(matches(isDisplayed()));

                    onView(withId(R.id.create_event)).perform(click());

                    //typing into create event
                    onView(withId(R.id.event_name)).perform(typeText("Edwards 3rd Birthday Party"));
                    onView(withId(R.id.event_class_capacity)).perform(typeText("40"));
                    onView(withId(R.id.event_waitlist_capacity)).perform(typeText("40"));
                    onView(withId(R.id.event_date)).perform(typeText("10-12-2025"));
                    onView(withId(R.id.event_time)).perform(typeText("4:00pm"));
                    onView(withId(R.id.event_instructor)).perform(typeText("Barry"));
                    onView(withId(R.id.event_description)).perform(typeText("Second Floor"));


                    //click confirm
                    onView(withId(R.id.event_confirm_button)).perform(click());
                    //see if it was passed--then the event was created
                    //onView(withId(R.id.event_confirm_button)).check(matches(not(isDisplayed())));

                }
            }

            @Override
            public void onReadFailure(Exception e) {
                onView(withId(R.id.welcome_home)).check(matches(isDisplayed()));
            }
        });
    }


    /**
     * Tests whether we can access the entrant map
     */
    @Test
    public void AccessEntrantMap(){
        String testId = UUID.randomUUID().toString();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean exists = new AtomicBoolean(false);


        CRUD.readStatic(testId, User.class, new ReadCallback<User>() {
            @Override
            public void onReadSuccess(User data) {
                onView(withId(R.id.welcome_home)).perform(click());

                //getting to the entrant page
                onView(withId(R.id.profile_spinner_entrant)).perform(click());
                onData(anything()).atPosition(1).perform(click());
                onView(withId(R.id.organizer_profile_button)).check(matches(isDisplayed()));

                //clicks on entrant
                onView(withId(R.id.mapped)).perform(click());

                //makes sure the page moves on
                //onView(withId(R.id.mapped)).check(matches(not(isDisplayed())));
            }

            @Override
            public void onReadFailure(Exception e) {
                //Only users can access this feature.
                onView(withId(R.id.welcome_home)).check(matches(isDisplayed()));
            }
        });

    }

}
