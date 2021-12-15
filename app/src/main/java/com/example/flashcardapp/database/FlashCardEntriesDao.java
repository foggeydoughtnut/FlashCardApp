package com.example.flashcardapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flashcardapp.models.FlashCardEntry;

import java.util.List;

@Dao
public interface FlashCardEntriesDao {
    @Insert
    public long insert(FlashCardEntry entry);

    @Query("SELECT * FROM flashcardentry")
    public List<FlashCardEntry> getAll();

    @Query("SELECT * FROM flashcardentry WHERE time_studied <= :timeStudied")
    public List<FlashCardEntry> getAllCurrent(long timeStudied);

    @Query("SELECT * FROM flashcardentry WHERE id = :id LIMIT 1")
    public FlashCardEntry findById(long id);

    @Query("SELECT * FROM flashcardentry WHERE status = :status")
    public List<FlashCardEntry> findAllByStatus(int status);

    @Update
    public void update(FlashCardEntry entry);

    @Delete
    public void delete(FlashCardEntry entry);
}
