package co.nayn.nayn.constants;

import android.appwidget.AppWidgetManager;

import com.ncapdevi.fragnav.FragNavController;

public final class NaynConstants {
    public static final int INDEX_ALL = FragNavController.TAB1;
    public static final int INDEX_WORLD = FragNavController.TAB2;
    public static final int INDEX_AGENDA = FragNavController.TAB3;
    public static final int INDEX_ART = FragNavController.TAB4;
    public static final int INDEX_HOT = FragNavController.TAB5;
    public static final int INDEX_SOCIAL = FragNavController.TAB6;
    public static final int INDEX_SPORT = FragNavController.TAB7;
    public static final int INDEX_SAVED = FragNavController.TAB8;
    public static final int CATEGORY_ID_ALL_NEWS = 10;
    public static final Integer CATEGORY_ID_SAVED = 9999;

    public static final String POD_URL = "http://nayn.co/pod";
    public static final String BASE_URL = "http://api.nayn.co/v1/";
    public static final String CATEGORY_ID = "category_id";
    public static final String POSTS_BY_CATEGORY = "posts-by-category/";
    public static final String POST_KEY = "post";




    public final class AppWidget{
        public static final int APP_WIDGET_ID = AppWidgetManager.INVALID_APPWIDGET_ID;
        public static final int APP_WIDGET_LIMIT = 20;
        public static final int APP_WIDGET_PAGE_NUMBER = 1;
    }

}
