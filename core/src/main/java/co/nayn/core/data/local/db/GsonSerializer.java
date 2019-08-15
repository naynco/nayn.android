package co.nayn.core.data.local.db;

public interface GsonSerializer<T> {

    T deserialize(String s, Class<T> type);

    String serialize(T instance);

}
