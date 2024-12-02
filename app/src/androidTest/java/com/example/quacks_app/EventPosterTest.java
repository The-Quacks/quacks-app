//package com.example.quacks_app;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.Espresso.pressBack;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static org.hamcrest.Matchers.containsString;
//
//import android.content.Intent;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import com.example.quacks_app.R;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//@RunWith(AndroidJUnit4.class)
//public class EventPosterTest {
//
//    private Intent mockIntent;
//
//    @Before
//    public void setUp() {
//        // Create a mock intent with the necessary extras
//        mockIntent = new Intent();
//        mockIntent.putExtra("Event", MockData.getMockEvent());
//        mockIntent.putExtra("User", MockData.getMockUser());
//        mockIntent.putExtra("Facility", MockData.getMockFacility());
//    }
//
//    @Test
//    public void testEventInfoDisplaysCorrectly() {
//        ActivityScenario.launch(EventInfo.class, mockIntent);
//
//        // Verify event name
//        onView(withId(R.id.event_name)).check(matches(withText(MockData.getMockEvent().getEventName())));
//
//        // Verify event description
//        onView(withId(R.id.event_description)).check(matches(withText(MockData.getMockEvent().getDescription())));
//
//        // Verify instructor name
//        onView(withId(R.id.instructor)).check(matches(withText(MockData.getMockEvent().getInstructor())));
//
//        // Verify waitlist and registration capacities
//        onView(withId(R.id.waitlist_capacity)).check(matches(withText(String.valueOf(MockData.getMockEvent().getWaitlistCapacity()))));
//        onView(withId(R.id.class_capacity)).check(matches(withText(String.valueOf(MockData.getMockEvent().getRegistrationCapacity()))));
//    }
//
//    @Test
//    public void testOpenRegistrationButtonNavigatesCorrectly() {
//        ActivityScenario.launch(EventInfo.class, mockIntent.getExtras());
//
//        onView(withId(R.id.register_button)).perform(click());
//        onView(withId(R.id.open_registration_layout)).check(matches(isDisplayed())); // Verify OpenRegistration screen
//    }
//
//    @Test
//    public void testApplicantOptionsButtonNavigatesCorrectly() {
//        ActivityScenario.launch(EventInfo.class, mockIntent);
//
//        onView(withId(R.id.applicant_lists)).perform(click());
//        onView(withId(R.id.applicant_options_layout)).check(matches(isDisplayed())); // Verify ApplicantOptions screen
//    }
//
//    @Test
//    public void testEditEventButtonOpensDialog() {
//        ActivityScenario.launch(EventInfo.class, mockIntent);
//
//        onView(withId(R.id.edit_event_button)).perform(click());
//        onView(withText("Choose an Action")).check(matches(isDisplayed())); // Verify dialog opens
//
//        // Select "Edit Event" and verify navigation
//        onView(withText("Edit Event")).perform(click());
//        onView(withId(R.id.edit_event_layout)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void testDeleteEventButtonShowsConfirmation() {
//        ActivityScenario.launch(EventInfo.class, mockIntent);
//
//        onView(withId(R.id.edit_event_button)).perform(click());
//        onView(withText("Choose an Action")).check(matches(isDisplayed()));
//
//        // Select "Delete Event" and confirm
//        onView(withText("Delete Event")).perform(click());
//        onView(withText("Confirm Delete")).check(matches(isDisplayed()));
//        onView(withText("Yes, Delete")).perform(click());
//
//        // Verify that the activity finishes (could also verify backend deletion if mockable)
//        onView(withId(R.id.event_info_layout)).check(doesNotExist());
//    }
//
//    @Test
//    public void testNavigationButtonsWorkCorrectly() {
//        ActivityScenario.launch(EventInfo.class, mockIntent);
//
//        // Test homepage button
//        onView(withId(R.id.house)).perform(click());
//        onView(withId(R.id.organizer_homepage_layout)).check(matches(isDisplayed()));
//
//        // Test profile button
//        onView(withId(R.id.person)).perform(click());
//        onView(withId(R.id.view_organizer_layout)).check(matches(isDisplayed()));
//
//        // Test search button
//        onView(withId(R.id.search)).perform(click());
//        pressBack(); // Verify it finishes current activity
//    }
//}
//
//
//
