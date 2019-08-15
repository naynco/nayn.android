package co.nayn.nayn.di.component;

import androidx.lifecycle.AndroidViewModel;

import co.nayn.nayn.MainApplication;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.nayn.di.module.ActivityBindingModule;
import co.nayn.nayn.di.module.AppModule;
import co.nayn.nayn.di.module.DataSourceModule;
import co.nayn.nayn.di.module.FragmentBindingModule;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {ActivityBindingModule.class, FragmentBindingModule.class , AppModule.class, DataSourceModule.class, AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(MainApplication application);

    void inject(AndroidViewModel androidViewModel);

    NaynDataSource naynDataSource();

}
