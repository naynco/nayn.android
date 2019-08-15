package co.nayn.nayn.di.module;

import android.content.Context;

import co.nayn.nayn.MainApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final MainApplication application;

    public AppModule(MainApplication application){
        this.application = application;
    }

    @Provides
    public Context providesContext(){
        return application;
    }

    @Provides
    public MainApplication provideApplication(){
        return application;
    }

}
