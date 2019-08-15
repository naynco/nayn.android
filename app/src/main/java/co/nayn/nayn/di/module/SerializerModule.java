package co.nayn.nayn.di.module;

import com.google.gson.Gson;

import co.nayn.core.data.local.db.Serializer;
import dagger.Module;
import dagger.Provides;

@Module
public class SerializerModule {
    @Provides
    static Serializer provideSerializer(Gson gson){ return new Serializer().setGson(gson); }
}
