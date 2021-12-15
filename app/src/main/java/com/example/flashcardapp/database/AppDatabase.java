package com.example.flashcardapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.flashcardapp.models.FlashCardEntry;

@Database(entities = {FlashCardEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FlashCardEntriesDao getFlashCardEntriesDao();
}
