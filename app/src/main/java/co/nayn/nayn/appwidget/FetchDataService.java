package co.nayn.nayn.appwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import co.nayn.core.data.model.Posts;
import co.nayn.core.data.remote.NaynDataSource;
import co.nayn.core.data.remote.NaynDataSourceBuilder;
import co.nayn.nayn.constants.NaynConstants;
import io.paperdb.Paper;

public class FetchDataService extends Service {
    private static final String TAG = "AppWidget";
    private int[] appWidgetIds = new int[]{};

    NaynDataSource dataSource;
    private static boolean isRunning = false;


    public static ArrayList<Posts> getPosts(Context context) {
        Paper.init(context);
        if (!Paper.book().contains("posts")) {
            return new ArrayList<>();
        }
        return Paper.book().read("posts");
    }

    public static synchronized boolean isRunning() {
        return isRunning;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        dataSource = new NaynDataSourceBuilder().with(this).build();
        Log.i(TAG, "start service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy service FetchDataService");
        isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID);
            fetch();
        } else {
            Log.i(TAG, "NO APP WIDGET ID");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void fetch() {
        AsyncTask.execute(() -> {
            Log.i(TAG, "Fetching data...");
            try {
                ArrayList<Posts> posts = dataSource.getAllPosts(NaynConstants.AppWidget.APP_WIDGET_PAGE_NUMBER, NaynConstants.AppWidget.APP_WIDGET_LIMIT).execute().body().getData();
                Paper.book().write("posts", posts);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bind remote views they could be multiple
            for (int appWidgetId : appWidgetIds) {
                Intent widgetUpdateIntent = new Intent(getApplicationContext(), NaynAppWidget.class);
                widgetUpdateIntent.setAction(NaynAppWidget.ACTION_DATA_FETCHED);
                widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                sendBroadcast(widgetUpdateIntent);
            }

            Log.i(TAG, "Fetched data");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            } else {
                stopSelf();
            }
        });
    }


}

