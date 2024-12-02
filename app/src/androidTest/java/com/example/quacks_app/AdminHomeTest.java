package com.example.quacks_app;

import static android.os.SystemClock.sleep;
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@LargeTest
@FixMethodOrder(MethodSorters. NAME_ASCENDING)
public class AdminHomeTest{
    @Rule
    public ActivityScenarioRule<AdminHome> scenario = new
            ActivityScenarioRule<AdminHome>(AdminHome.class);

    @Before
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(12);
        System.setProperty("junit.test", "true");
        UpdateCallback upCall = new UpdateCallback() {
            @Override
            public void onUpdateSuccess() {
                latch.countDown();
            }

            @Override
            public void onUpdateFailure(Exception e) {
                latch.countDown();
            }
        };

        // setup test data
        Event event = new Event();
        event.setDescription("Some event");
        event.setDocumentId("IxP9RUJUcm9UF7trq635");
        event.setApplicantList("ABCDEFG");
        event.setFacility("HIJKLMNOP");
        CRUD.createOrUpdate(event, upCall);
        ApplicantList applicantList = new ApplicantList();
        applicantList.setDocumentId("ABCDEFG");
        applicantList.setLimit(20);
        CRUD.createOrUpdate(applicantList, upCall);

        Facility facility = new Facility();
        facility.setName("CoolEvent");
        facility.setOrganizerId("ahahaha");
        facility.setDocumentId("HIJKLMNOP");
        CRUD.createOrUpdate(facility, upCall);
        User user = new User();
        user.setDocumentId("ahahaha");
        UserProfile userProf = new UserProfile();
        userProf.setUserName("Aiden");
        userProf.setEmail("peacock@asd.cd");
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(Role.ENTRANT);
        user.setRoles(roles);
        user.setUserProfile(userProf);
        CRUD.createOrUpdate(user, upCall);


        Event event1 = new Event();
        event1.setDescription("Some event1");
        event1.setDocumentId("IxD9RUJUcm9UF7trq635");
        event1.setApplicantList("ABCDEFGH");
        event1.setFacility("IJKLMNOPQ");
        CRUD.createOrUpdate(event1, upCall);
        ApplicantList applicantList1 = new ApplicantList();
        applicantList1.setDocumentId("ABCDEFGH");
        applicantList1.setLimit(20);
        CRUD.createOrUpdate(applicantList1, upCall);

        Facility facility1 = new Facility();
        facility1.setName("CoolFacility");
        facility1.setOrganizerId("ohohoho");
        facility1.setDocumentId("IJKLMNOPQ");
        CRUD.createOrUpdate(facility1, upCall);
        User user1 = new User();
        user1.setDocumentId("ohohoho");
        UserProfile userProf1 = new UserProfile();
        userProf1.setUserName("Aiden");
        userProf1.setEmail("peacock@asd.cd");
        ArrayList<Role> roles1 = new ArrayList<>();
        roles1.add(Role.ENTRANT);
        user1.setRoles(roles1);
        user1.setUserProfile(userProf1);
        CRUD.createOrUpdate(user1, upCall);

        Event event2 = new Event();
        event2.setDescription("Some event2");
        event2.setDocumentId("IxC9RUJUcm9UF7trq635");
        event2.setApplicantList("ABCDEFGHI");
        event2.setFacility("JKLMNOPQR");
        CRUD.createOrUpdate(event2, upCall);
        ApplicantList applicantList2 = new ApplicantList();
        applicantList2.setDocumentId("ABCDEFGH");
        applicantList2.setLimit(20);
        CRUD.createOrUpdate(applicantList2, upCall);

        Facility facility2 = new Facility();
        facility2.setName("CoolFacility1");
        facility2.setOrganizerId("ehehehe");
        facility2.setDocumentId("JKLMNOPQR");
        CRUD.createOrUpdate(facility2, upCall);
        User user2 = new User();
        user2.setDocumentId("ehehehe");
        UserProfile userProf2 = new UserProfile();
        userProf2.setUserName("Aiden");
        userProf2.setEmail("peacock@asd.cd");
        ArrayList<Role> roles2 = new ArrayList<>();
        roles2.add(Role.ENTRANT);
        user2.setRoles(roles1);
        user2.setUserProfile(userProf2);
        CRUD.createOrUpdate(user2, upCall);
        latch.await();
    }

    @After
    public void tearDown() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(4);
        DeleteCallback delCall = new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                latch.countDown();
            }

            @Override
            public void onDeleteFailure(Exception e) {
                latch.countDown();
            }
        };

        CRUD.clearColection(User.class, delCall);

        CRUD.clearColection(Event.class, delCall);

        CRUD.clearColection(ApplicantList.class, delCall);

        CRUD.clearColection(Facility.class, delCall);


        latch.await();
    }

    @Test
    public void atestBtnEvent(){
    onView(withId(R.id.eventsButton)).perform(click());
    onView(withId(R.id.eventsButton)).check(doesNotExist());

    }

    @Test
    public void atestEventList(){
        onView(withId(R.id.eventsButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.gen_list)).check(doesNotExist());
        onView(withId(R.id.Name)).check(matches(isDisplayed()));
    }

    @Test
    public void btestEventDelete() {
        onView(withId(R.id.eventsButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.delete_button)).perform(click());
    }


    @Test
    public void atestBtnImage(){
        onView(withId(R.id.imagesButton)).perform(click());
        onView(withId(R.id.imagesButton)).check(doesNotExist());
    }

    @Test
    public void atestBtnUser(){
        onView(withId(R.id.profilesButton)).perform(click());
        onView(withId(R.id.profilesButton)).check(doesNotExist());
    }

    @Test
    public void atestUserList(){
        onView(withId(R.id.profilesButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.gen_list)).check(doesNotExist());
        onView(withId(R.id.Name)).check(matches(isDisplayed()));
    }

    @Test
    public void dtestUserDelete() {
        onView(withId(R.id.profilesButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.delete_button)).perform(click());

    }

    @Test
    public void atestBtnFacility(){
        onView(withId(R.id.facilitiesButton)).perform(click());
        onView(withId(R.id.facilitiesButton)).check(doesNotExist());
    }

    @Test
    public void atestFacilityList(){
        onView(withId(R.id.facilitiesButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.gen_list)).check(doesNotExist());
        onView(withId(R.id.Name)).check(matches(isDisplayed()));
    }

    @Test
    public void ctestFacilityDelete() {
        onView(withId(R.id.facilitiesButton)).perform(click());
        sleep(500);
        onData(is(anything())).inAdapterView(withId(R.id.gen_list
        )).atPosition(0).perform(click());
        onView(withId(R.id.delete_button)).perform(click());

    }






}
