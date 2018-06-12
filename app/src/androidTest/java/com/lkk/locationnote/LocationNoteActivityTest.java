package com.lkk.locationnote;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LocationNoteActivityTest {
    @Rule
    public ActivityTestRule<LocationNoteActivity> mActivityRule =
            new ActivityTestRule<>(LocationNoteActivity.class);

    @Test
    public void bottomNavigationSelectedTest() {
        onView(withId(R.id.item_map)).perform(click());
        onView(withId(R.id.item_note)).perform(click());
        onView(withId(R.id.item_settings)).perform(click());
    }
}
