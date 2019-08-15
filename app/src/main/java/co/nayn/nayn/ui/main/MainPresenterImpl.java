package co.nayn.nayn.ui.main;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public abstract class MainPresenterImpl<T> implements MainPresenter,LifecycleObserver {

    public T mainView;

    public MainPresenterImpl(T mainView) {
        this.mainView = mainView;
        if (mainView instanceof LifecycleOwner) {
            ((LifecycleOwner) mainView).getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @Override
    public void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Override
    public void onDestroy() {
        getLifeCycle().removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @Override
    public void onPause() {

    }

    public Lifecycle getLifeCycle() {
        return ((LifecycleOwner) mainView).getLifecycle();
    }

}
