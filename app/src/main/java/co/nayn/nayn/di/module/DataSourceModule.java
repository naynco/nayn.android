package co.nayn.nayn.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.readystatesoftware.chuck.ChuckInterceptor;

import javax.inject.Singleton;

import androidx.room.Room;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.core.data.remote.NaynDataSourceBuilder;
import co.nayn.nayn.data.local.db.AppDatabase;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = {GsonModule.class, SerializerModule.class})
public class DataSourceModule {

    @Provides
    NaynDataSourceBuilder provideDataSourceBuilder(Context context, Gson gson){
        return new NaynDataSourceBuilder().with(context).with(gson);
    }


    @Provides
    OkHttpClient provideOkHttpClient(NaynDataSourceBuilder builder){
        return builder.OkHttpClient();
    }

    @Provides
    NaynDataSource provideNaynDataSource(NaynDataSourceBuilder builder){
        return builder.build();
    }


    @Provides
    AppDatabase provideDatabase(Context context){
        return Room.databaseBuilder(context,
                AppDatabase.class, "naynco_db").build();
    }

}
