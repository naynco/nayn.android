package co.nayn.nayn.data.local.db;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.room.TypeConverter;
import co.nayn.core.data.model.Attachment;
import co.nayn.core.data.model.Category;
import co.nayn.core.data.model.Images;

public class Converters {

    @TypeConverter
    public static Images stringToImages(String mediaListString) {
        Type myType = new TypeToken<Images>() {}.getType();
        return new Gson().fromJson(mediaListString, myType);
    }

    @TypeConverter
    public static String imagesToString(Images mediaItems) {
        Gson gson = new Gson();
        Type type = new TypeToken<Images>() {
        }.getType();
        String json = gson.toJson(mediaItems, type);
        return json;
    }

    @TypeConverter
    public static String arrayToAttachment(ArrayList<Attachment> object) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Attachment>>() {
        }.getType();
        String json = gson.toJson(object, type);
        return json;
    }

    @TypeConverter
    public static ArrayList<Attachment> stringToAttachment(String string) {
        Type myType = new TypeToken<ArrayList<Attachment>>() {}.getType();
        return new Gson().fromJson(string, myType);
    }

    @TypeConverter
    public static String arrayToString(ArrayList<Category> object) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        String json = gson.toJson(object, type);
        return json;
    }

    @TypeConverter
    public static ArrayList<Category> stringToCategory(String string) {
        Type myType = new TypeToken<ArrayList<Category>>() {}.getType();
        return new Gson().fromJson(string, myType);
    }


}
