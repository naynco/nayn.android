package co.nayn.nayn.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {
    @Provides
    static Gson provideGson(){ return new GsonBuilder().create(); }
}
