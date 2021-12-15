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
import java.util.concurrent.CountDownLatch;

public class FlashCardViewModel extends AndroidViewModel {

    private AppDatabase database;
    private MutableLiveData<Boolean> saving = new MutableLiveData<>();
    private ObservableArrayList<FlashCardEntry> entries = new ObservableArrayList<>();
    private ObservableArrayList<FlashCardEntry> status0 = new ObservableArrayList<>();
    private ObservableArrayList<FlashCardEntry> status1 = new ObservableArrayList<>();
    private ObservableArrayList<FlashCardEntry> status2 = new ObservableArrayList<>();

    // This allows the fragments that use the data to wait to access the data until it has finished loading
    // I used the following as a reference https://stackoverflow.com/questions/4691533/java-wait-for-thread-to-finish,
    // and my brother (in CS3100) helped explain what is going on when I use this
    public CountDownLatch doneLoading = new CountDownLatch(4);

    public FlashCardViewModel(@NonNull Application application) {
        super(application);
        saving.setValue(false);
        database = Room.databaseBuilder(application, AppDatabase.class, "flashcarddb").build();

        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().getAll();
            entries.addAll(flashCardEntries);
            doneLoading.countDown();
        }).start();

        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().findAllByStatus(0);
            status0.addAll(flashCardEntries);
            doneLoading.countDown();
        }).start();
        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().findAllByStatus(1);
            status1.addAll(flashCardEntries);
            doneLoading.countDown();
        }).start();
        new Thread(() -> {
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().findAllByStatus(2);
            status2.addAll(flashCardEntries);
            doneLoading.countDown();
        }).start();
    }

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public ObservableArrayList<FlashCardEntry> getEntries() {
        return entries;
    }

    public ObservableArrayList<FlashCardEntry> getStatusNumber(int status){
        switch(status){
            case 0:
                return status0;
            case 1:
                return status1;
            case 2:
                return status2;
            default:
                return null;
        }
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

            entries.add(newEntry);

            // put into a list

        }).start();



    }

    public void updateCard(FlashCardEntry card){
        database.getFlashCardEntriesDao().update(card);
    }
}
