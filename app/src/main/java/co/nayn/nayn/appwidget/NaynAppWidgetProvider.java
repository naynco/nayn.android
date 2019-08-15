package co.nayn.nayn.appwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import co.nayn.core.data.model.Posts;
import co.nayn.nayn.R;
import co.nayn.nayn.ui.splash.SplashActivity;

public class NaynAppWidgetProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "AppWidget";
    Context context;

    public NaynAppWidgetProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate@NaynAppWidgetProvider");
        Log.i(TAG, "size of posts = " + FetchDataService.getPosts(context).size());
    }

    @Override
    public void onDataSetChanged() {
        Log.i(TAG, "onDatassetChanged@NaynAppWidgetProvider");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDatassetChanged@onDestroy");
    }

    @Override
    public int getCount() {
        return FetchDataService.getPosts(context).size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(context.getPackageName(),
                R.layout.widget_news_layout);

        Posts post = FetchDataService.getPosts(context).get(position);

        view.setTextViewText(android.R.id.text1, FetchDataService.getPosts(context).get(position).getTitle());

        try {
            Bitmap bitmap = Picasso.get().load(post.getImages().getSmall()).get();
            view.setImageViewBitmap(android.R.id.icon,bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent activityIntent = new Intent(context, SplashActivity.class);
        activityIntent.setData(Uri.parse(post.getUrl()));

        view.setOnClickFillInIntent(android.R.id.text1, activityIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.progressbar);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return FetchDataService.getPosts(context).get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}
