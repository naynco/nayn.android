package co.nayn.nayn.di.module;

import co.nayn.nayn.ui.detail.NewsDetailFragment;
import co.nayn.nayn.ui.navigation.NavigationFragment;
import co.nayn.nayn.ui.news.NewsFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {
    @ContributesAndroidInjector()
    abstract NavigationFragment navigationFragment();
    @ContributesAndroidInjector()
    abstract NewsDetailFragment newsDetailFragment();
    @ContributesAndroidInjector()
    abstract NewsFragment newsFragment();

}
