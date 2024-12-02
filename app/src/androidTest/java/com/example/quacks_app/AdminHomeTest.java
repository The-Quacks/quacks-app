package com.example.quacks_app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static
        androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@LargeTest
public class AdminHomeTest {
    @Rule
    public ActivityScenarioRule<AdminHome> scenario = new
            ActivityScenarioRule<AdminHome>(AdminHome.class);

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

    @Test
    public void testBtnEvent(){
    onView(withId(R.id.eventsButton)).perform(click());
    onView(withId(R.id.eventsButton)).check(doesNotExist());

    }

    @Test
    public void testEventList(){
        onView(withId(R.id.eventsButton)).perform(click());
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.gen_list)).check(doesNotExist());
        onView(withId(R.id.Name)).check(matches(isDisplayed()));
    }

    @Test
    public void testBtnImage(){
        onView(withId(R.id.imagesButton)).perform(click());
        onView(withId(R.id.eventsButton)).check(doesNotExist());
    }

    @Test
    public void testImageList(){
        onView(withId(R.id.imagesButton)).perform(click());
        onData(is(anything())).inAdapterView(withId(R.id.image_list_view
        )).atPosition(0).perform(click());
    }



}
