package co.nayn.nayn.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import co.nayn.nayn.R;
import co.nayn.nayn.ui.splash.SplashActivity;

/**
 * Implementation of App Widget functionality.
 */
public class NaynAppWidget extends AppWidgetProvider {
    public static final String ACTION_DATA_FETCHED = "co.nayn.widget.DATA_FETCHED";
    String TAG = "AppWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.i(TAG, "onUpdate@NaynAppWidget");
        startFetchService(context, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void refreshData(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        startFetchService(context, appWidgetManager.getAppWidgetIds(new ComponentName(context, NaynAppWidget.class)));
    }


    private synchronized void startFetchService(Context context, int[] appWidgetIds) {
        if (FetchDataService.isRunning()) {
            Log.i(TAG, "Service is running we wait");
            return;
        }
        Intent serviceIntent = new Intent(context, FetchDataService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetIds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.nayn_app_widget);
        Intent refreshIntent = new Intent(context, NaynAppWidget.class);
        refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
                refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list, refreshPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);

        if(FetchDataService.getPosts(context).size() > 0){
            remoteViews.setViewVisibility(R.id.progress_bar, View.GONE);
        }
        //set click template
        Intent activityIntent = new Intent(context, SplashActivity.class);
        // Set the action for the intent.
        // When the user touches a particular view.
        activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

        // RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, NaynWidgetService.class);

        // passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // setting a unique Uri to the intent

        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));


        // setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.widget_list, svcIntent);

        // setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.widget_list, R.id.progress_bar);


        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i(TAG, "receive action " + intent.getAction());
        if (intent.getAction().equals(ACTION_DATA_FETCHED) || intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")) {

            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);

            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);

            // update and notify widget when data arrives
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                    R.id.widget_list);

        }
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Log.i(TAG, "HERE");
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            startFetchService(context,new int[]{appWidgetId});
        }

    }
}

