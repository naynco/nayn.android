package co.nayn.news;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.FailureHandler;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.squareup.picasso.PicassoIdlingResource;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import co.nayn.nayn.MainApplication;
import co.nayn.nayn.R;
import co.nayn.nayn.ui.main.MainActivity;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;
import tools.fastlane.screengrab.locale.LocaleTestRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class Screenshot {
    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        PicassoIdlingResource  mPicassoIdlingResource = new PicassoIdlingResource();
        IdlingRegistry.getInstance().register(mPicassoIdlingResource);
        ActivityLifecycleMonitorRegistry
                .getInstance()
                .addLifecycleCallback(mPicassoIdlingResource);

        OkHttp3IdlingResource okHttp3IdlingResource =  OkHttp3IdlingResource.create("okhttp",((MainApplication)activityRule.getActivity().getApplication()).getOkHttpClient());
        IdlingRegistry.getInstance().register(okHttp3IdlingResource);

    }


    @Test
    public void testTakeScreenshot() {
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Screengrab.screenshot("main_screen");


        onView(withId(R.id.news_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Screengrab.screenshot("news_screen");

        onView(withId(R.id.news_detail_scroll_view))
                .perform(swipeUp());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Screengrab.screenshot("news_screen_bottom");

    }
}

