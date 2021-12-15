package com.example.flashcardapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.flashcardapp.database.AppDatabase;
import com.example.flashcardapp.models.FlashCardEntry;

import java.util.ArrayList;
import java.util.Collections;

public class FlashCardViewModel extends AndroidViewModel {

    private AppDatabase database;
    private MutableLiveData<Boolean> saving = new MutableLiveData<>();
    private ObservableArrayList<FlashCardEntry> entries = new ObservableArrayList<>();

    public FlashCardViewModel(@NonNull Application application) {
        super(application);
        saving.setValue(false);
        database = Room.databaseBuilder(application, AppDatabase.class, "flashcarddb").build();

        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().getAll();

            entries.addAll(flashCardEntries);
        }).start();

        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().getAll();
            entries.addAll(flashCardEntries);
        }).start();
    }

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public ObservableArrayList<FlashCardEntry> getEntries() {


        return entries;
    }

    public void saveFlashCard(String front, String back) {
        FlashCardEntry newEntry = new FlashCardEntry();

        new Thread(() -> {
            newEntry.front = front;
            newEntry.back = back;
            newEntry.createdAt = System.currentTimeMillis();
            newEntry.status = 0;
            // insert into database
            newEntry.id = database.getFlashCardEntriesDao().insert(newEntry);


            // put into a list

        }).start();



    }
}
