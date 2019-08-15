package co.nayn.nayn.data.local.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SavedNewsDao {

    @Query("SELECT * FROM SavedNew")
    List<SavedNew> getAll();

    @Insert
    void insertAll(SavedNew... users);

    @Delete
    void delete(SavedNew user);

    @Query("SELECT COUNT(*) FROM SavedNew WHERE id == (:id)")
    int count(int id);

}
