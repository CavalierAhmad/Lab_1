package algonquin.cst2335.alja0062;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /** Elements on which tests will be conducted */
    ViewInteraction textView = onView(withId(R.id.text));
    ViewInteraction editText = onView( withId(R.id.edit));
    ViewInteraction button = onView(withId(R.id.btn));

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests if Textview gets updated upon entering an erroneous password
     */
    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        editText
                .perform(click())
                .perform(replaceText("12345"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a missing upper case character is detected
     */
    @Test
    public void testFindMissingUpperCase(){
        editText.perform(replaceText("a1@"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a missing lower case character is detected
     */
    @Test
    public void testFindMissingLowerCase(){
        editText.perform(replaceText("A1@"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a missing number in the password is detected
     */
    @Test
    public void testFindMissingNumber(){
        editText.perform(replaceText("Aa@"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a missing special character is detected
     */
    @Test
    public void testFindMissingSpecial(){
        editText.perform(replaceText("Aa1"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Tests that a password meeting all requirements is accepted.
     */
    @Test
    public void testRequirementsMet(){
        editText.perform(replaceText("Aa1@"), closeSoftKeyboard());
        button.perform(click());
        textView.check(matches(withText("Your password meets all the requirement")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
