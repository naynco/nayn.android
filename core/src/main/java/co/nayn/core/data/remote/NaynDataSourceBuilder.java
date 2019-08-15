package co.nayn.core.data.remote;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

import co.nayn.core.NaynConstants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaynDataSourceBuilder {

    private Context context;
    private Gson gson;
    private Set<Interceptor> interceptors;

    private static OkHttpClient okHttpClient;

    public NaynDataSourceBuilder(){
    }

    public NaynDataSourceBuilder with(Context context){
        this.context = context;
        return this;
    }

    public NaynDataSourceBuilder with(Interceptor interceptor){
        if(interceptors == null){
            interceptors = new HashSet<>();
        }
        interceptors.add(interceptor);
        return this;
    }

    public NaynDataSource build(Gson gson){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NaynConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClient())
                .build();
        return retrofit.create(NaynDataSource.class);
    }

    public NaynDataSource build(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NaynConstants.BASE_URL)
                .addConverterFactory(gson != null?GsonConverterFactory.create(gson):GsonConverterFactory.create())
                .client(OkHttpClient())
                .build();
        return retrofit.create(NaynDataSource.class);
    }

    public OkHttpClient OkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addNetworkInterceptor(new StethoInterceptor());
        if(interceptors != null){
            for(Interceptor interceptor : interceptors){
                builder.addNetworkInterceptor(interceptor);
            }
        }
        okHttpClient = builder.build();
        return builder.build();
    }


    public NaynDataSourceBuilder with(Gson gson) {
        this.gson = gson;
        return this;
    }
}
