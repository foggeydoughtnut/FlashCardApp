package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.flashcardapp.fragments.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_contatiner, main_fragment.class, null)
                    .commit();
        }
    }
}