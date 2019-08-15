package co.nayn.nayn.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

public class NaynWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NaynAppWidgetProvider(this);
    }
}
