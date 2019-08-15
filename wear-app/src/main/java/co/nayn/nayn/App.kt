package co.nayn.nayn

import android.app.Application
import co.nayn.core.data.remote.NaynDataSource
import co.nayn.core.data.remote.NaynDataSourceBuilder
import com.facebook.stetho.Stetho

class App: Application() {

    lateinit var dataSource: NaynDataSource

    override fun onCreate() {
        super.onCreate()
        dataSource = NaynDataSourceBuilder()
                .with(this)
                .build()
        Stetho.initializeWithDefaults(this)
    }


}