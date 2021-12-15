package com.example.flashcardapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class FlashCardEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public String front;

    @ColumnInfo
    public String back;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "status")
    public int status;

    @ColumnInfo(name = "time_studied")
    public long timeStudied;
}
