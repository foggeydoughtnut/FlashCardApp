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
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FlashCardViewModel extends AndroidViewModel {

    private AppDatabase database;
    private ObservableArrayList<FlashCardEntry> entries = new ObservableArrayList<>();
    private boolean isNew;

    // This allows the fragments that use the data to wait to access the data until it has finished loading
    // I used the following as a reference https://stackoverflow.com/questions/4691533/java-wait-for-thread-to-finish,
    // and my brother (in CS3100) helped explain what is going on when I use this
    public CountDownLatch doneLoading = new CountDownLatch(1);

    public FlashCardViewModel(@NonNull Application application) {
        super(application);
        database = Room.databaseBuilder(application, AppDatabase.class, "flashcarddb").build();
        isNew = true;
        refresh();
    }

    public void refresh(){
        entries.clear();
        doneLoading = new CountDownLatch(1);
        new Thread(() -> {
            // One day in milliseconds 86400000
            ArrayList<FlashCardEntry> flashCardEntries = (ArrayList<FlashCardEntry>) database.getFlashCardEntriesDao().getAllCurrent(System.currentTimeMillis() - 30000);
            entries.addAll(flashCardEntries);
            doneLoading.countDown();
        }).start();
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
            newEntry.timeStudied = 0;
            // insert into database
            newEntry.id = database.getFlashCardEntriesDao().insert(newEntry);

            entries.add(newEntry);


            // put into a list

        }).start();
    }

    public void updateCard(FlashCardEntry card) {
        new Thread(() -> {
            database.getFlashCardEntriesDao().update(card);
        }).start();
    }

    public void studiedCard(FlashCardEntry card){
        new Thread(() -> {
            card.timeStudied = System.currentTimeMillis();
            card.status = 2;
            database.getFlashCardEntriesDao().update(card);
        }).start();
    }

    // Created this with some help from my brother
    public FutureTask<FlashCardEntry> getById(Long id){
        FutureTask<FlashCardEntry> task = new FutureTask<>(new Callable<FlashCardEntry>() {
            @Override
            public FlashCardEntry call() throws Exception {
                return database.getFlashCardEntriesDao().findById(id);
            }
        });
        new Thread(task).start();
        return task;
    }
}
