package com.example.quacks_app;

import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class JoinWaitlistTest {
    @Rule
    public ActivityScenarioRule<WelcomeEntrant> activityRule = new ActivityScenarioRule<>(WelcomeEntrant.class);

    @BeforeClass
    public static void setup() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        System.setProperty("junit.test", "true");

        // setup test data
        Event event = new Event();
        event.setDescription("Some event");
        event.setDocumentId("IxP9RUJUcm9UF7trq635");
        event.setApplicantList("ABCDEFG");
        CRUD.createOrUpdate(event, new UpdateCallback() {
            @Override
            public void onUpdateSuccess() {
                latch.countDown();
            }

            @Override
            public void onUpdateFailure(Exception e) {
                latch.countDown();
            }
        });
        ApplicantList applicantList = new ApplicantList();
        applicantList.setDocumentId("ABCDEFG");
        CRUD.createOrUpdate(applicantList, new UpdateCallback() {
            @Override
            public void onUpdateSuccess() {
                latch.countDown();
            }

            @Override
            public void onUpdateFailure(Exception e) {
                latch.countDown();
            }
        });
        latch.await();
    }


    @AfterClass
    public static void tearDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

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

        CRUD.clearColection(Event.class, new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                latch.countDown();
            }

            @Override
            public void onDeleteFailure(Exception e) {
                latch.countDown();
            }
        });

        CRUD.clearColection(ApplicantList.class, new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                latch.countDown();
            }

            @Override
            public void onDeleteFailure(Exception e) {
                latch.countDown();
            }
        });


        latch.await();
    }

    // This instrumented test creates a new user profile, "scans" an event QR code, and then joins
    // the waitlist for that event. Actually scanning/using a QR code in a test is not easily done,
    // so the same information is instead hardcoded here and passed to the even activity.
    @Test
    public void testJoinWaitlist() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        sleep(1000);
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent switchActivityIntent = new Intent(context, EventDescription.class);
        switchActivityIntent.putExtra("id", "IxP9RUJUcm9UF7trq635");

        try (ActivityScenario<EventDescription> scenario = ActivityScenario.launch(switchActivityIntent)) {
            sleep(1000);
            onView(withId(R.id.eventTitle)).check(matches(isDisplayed()));
            sleep(1000);
            onView(withId(R.id.joinWaitlistButton)).perform(click());
            scenario.close();
        }

        onView(withId(R.id.welcomeTitleEntrant)).check(matches(isDisplayed()));

        // Check if applicant actually is in list
        CRUD.readStatic("ABCDEFG", ApplicantList.class, new ReadCallback<ApplicantList>() {
            @Override
            public void onReadSuccess(ApplicantList data) {
                data.getApplicantIds().size();
                assert(data.getApplicantIds().size() == 1);
                latch.countDown();
            }

            @Override
            public void onReadFailure(Exception e) {
                assert(false);
                latch.countDown();
            }
        });
        latch.await();
    }
}
