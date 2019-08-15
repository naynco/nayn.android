package com.squareup.picasso;

import android.app.Activity;
import android.os.Handler;
import androidx.test.espresso.IdlingResource;
import androidx.test.runner.lifecycle.ActivityLifecycleCallback;
import androidx.test.runner.lifecycle.Stage;
import android.util.Log;

import java.lang.ref.WeakReference;

public class PicassoIdlingResource implements IdlingResource, ActivityLifecycleCallback {

  private static final int IDLE_POLL_DELAY_MILLIS = 100;
  
  protected ResourceCallback callback;

  WeakReference<Picasso> picassoWeakReference;

  @Override
  public String getName() {
    return "PicassoIdlingResource";
  }

  @Override
  public boolean isIdleNow() {
    if (isIdle()) {
      notifyDone();
      return true;
    } else {
      /* Force a re-check of the idle state in a little while.
       * If isIdleNow() returns false, Espresso only polls it every few seconds which can slow down our tests.
       */
      new Handler().postDelayed(() -> isIdleNow(), IDLE_POLL_DELAY_MILLIS);
      return false;
    }
  }

  public boolean isIdle() {
    Log.i("ISIDLE",(picassoWeakReference == null
            || picassoWeakReference.get() == null
            || picassoWeakReference.get().targetToAction.isEmpty())+"");
    return picassoWeakReference == null
            || picassoWeakReference.get() == null
            || picassoWeakReference.get().targetToAction.isEmpty();
  }

  @Override
  public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
    this.callback = resourceCallback;
  }

  void notifyDone() {
    if (callback != null) {
      callback.onTransitionToIdle();
    }
  }

  @Override
  public void onActivityLifecycleChanged(Activity activity, Stage stage) {
    switch (stage) {
      case RESUMED:
        picassoWeakReference = new WeakReference<>(Picasso.get());
        break;
      case PAUSED:
        // Clean up reference
        picassoWeakReference = null;
        break;
      default: // NOP
    }
  }
}