package com.example.quacks_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.ListView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import org.junit.Rule;
import org.junit.Test;


public class AdminHomeTest {
//    @Rule
//    public ActivityScenarioRule<AdminHome> scenario = new
//            ActivityScenarioRule<AdminHome>(AdminHome.class);

    @Test
    public void testBtnEvent(){
//        onView(withId(R.id.eventsButton)).perform(click());
//        onView(withId(R.id.eventsButton)).check(doesNotExist());
    }

    @Test
    public void testEventList(){
//        ListView genList;
//        CustomAdapter genAdapter;

    }
}
