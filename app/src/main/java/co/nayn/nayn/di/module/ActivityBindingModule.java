package co.nayn.nayn.di.module;

import co.nayn.nayn.ui.main.MainActivity;
import co.nayn.nayn.ui.splash.SplashActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {DataSourceModule.class})
    abstract MainActivity mainActivity();
    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();

}
