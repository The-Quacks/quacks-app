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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
public class AdminHomeTest {
    @Rule
    public ActivityScenarioRule<AdminHome> scenario = new
            ActivityScenarioRule<AdminHome>(AdminHome.class);

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


}
