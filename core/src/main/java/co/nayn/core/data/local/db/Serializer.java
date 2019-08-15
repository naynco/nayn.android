package co.nayn.core.data.local.db;

import com.google.gson.Gson;

public class Serializer implements GsonSerializer {

    Gson gson;

    public Serializer setGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    @Override
    public Object deserialize(String s,Class type) {
        return gson.fromJson(s, type);
    }

    @Override
    public String serialize(Object instance) {
        return gson.toJson(instance);
    }

}
