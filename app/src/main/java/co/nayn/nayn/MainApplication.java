package co.nayn.nayn;

import android.content.Context;
import androidx.annotation.VisibleForTesting;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;
import com.thefinestartist.Base;

import javax.inject.Inject;

import co.nayn.nayn.di.component.AppComponent;
import co.nayn.nayn.di.component.DaggerAppComponent;
import co.nayn.nayn.di.module.AppModule;
import co.nayn.nayn.di.module.DataSourceModule;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.fabric.sdk.android.Fabric;
import io.paperdb.Paper;
import okhttp3.OkHttpClient;

public class MainApplication extends DaggerApplication {
    @Inject
    OkHttpClient okHttpClient;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent naynComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataSourceModule(new DataSourceModule())
                .build();
        naynComponent.inject(this);
        return naynComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Base.initialize(this);
        Paper.init(this);
        initLibraries();
    }

    @VisibleForTesting
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void initLibraries(){
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Fabric.with(this, new Crashlytics());
        FacebookSdk.sdkInitialize(this);
        Stetho.initializeWithDefaults(this);
    }
}
