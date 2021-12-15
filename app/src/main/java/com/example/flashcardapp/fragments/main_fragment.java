package com.example.flashcardapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flashcardapp.R;
import com.example.flashcardapp.models.FlashCardEntry;
import com.example.flashcardapp.viewmodels.FlashCardViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class main_fragment extends Fragment {

    private boolean isDataLoaded = false;

    public main_fragment() {
        super(R.layout.fragment_main_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlashCardViewModel viewModel = new ViewModelProvider(getActivity()).get(FlashCardViewModel.class);
        ObservableArrayList<FlashCardEntry> flashcardEntries = viewModel.getEntries();
        TextView term  = view.findViewById(R.id.Term);
        TextView answer = view.findViewById(R.id.Answer);
        flashcardEntries.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<FlashCardEntry>>() {
            @Override
            public void onChanged(ObservableList<FlashCardEntry> sender) {
                if (!isDataLoaded){
                    isDataLoaded = true;
                    term.setText(flashcardEntries.get(0).front);
                    answer.setText(flashcardEntries.get(0).back);
                }
            }

            @Override
            public void onItemRangeChanged(ObservableList<FlashCardEntry> sender, int positionStart, int itemCount) { }
            @Override
            public void onItemRangeInserted(ObservableList<FlashCardEntry> sender, int positionStart, int itemCount) { }

            @Override
            public void onItemRangeMoved(ObservableList<FlashCardEntry> sender, int fromPosition, int toPosition, int itemCount) { }

            @Override
            public void onItemRangeRemoved(ObservableList<FlashCardEntry> sender, int positionStart, int itemCount) { }
        });

        // TODO try to figure out a way to make it so it doesn't have to wait for the array to get the data by using thread.sleep
        // TODO this also causes an infinite loop when the database is empty


        Collections.shuffle(flashcardEntries);

        // Set text to the correct card text



        FragmentManager fragManager = getParentFragmentManager();

        // TOP BAR LOGIC
        TextView decksButton = view.findViewById(R.id.DecksButton);
        TextView addButton = view.findViewById(R.id.AddButton);
        TextView editButton = view.findViewById(R.id.EditButton);

        decksButton.setOnClickListener((view1) -> {
            System.out.println("DECKS CLICKED");
            flashcardEntries.forEach(thing -> {
                System.out.printf("%s || ", thing.front);
            });
            System.out.println();
        });

        addButton.setOnClickListener(view1 -> {
            System.out.println("ADD CLICKED");
            fragManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_contatiner, add_fragment.class, null)
                    .commit();
        });

        editButton.setOnClickListener(view1 -> {
            System.out.println("EDIT CLICKED");
        });

        // CARD LOGIC
        LinearLayout card = view.findViewById(R.id.Card);
        LinearLayout bottomBar = view.findViewById(R.id.bottomBar);
        LinearLayout againGoodBottomBar = view.findViewById(R.id.AgainGoodBar);

        bottomBar.setVisibility(View.VISIBLE); /* If I comment this out, then it will work as it should, however, the bottom-bar won't
        be visible */



        card.setOnClickListener(view1 -> {
            // Show answer
            answer.setVisibility(View.VISIBLE);
            System.out.println("CARD CLICKED");

            // Display AgainGood bar
            bottomBar.setVisibility(View.INVISIBLE);
            againGoodBottomBar.setVisibility(View.VISIBLE);
        });

        // BOTTOM BAR LOGIC
        Button again = view.findViewById(R.id.Again);
        Button good = view.findViewById(R.id.Good);


        again.setOnClickListener(view1 -> {
            System.out.println("AGAIN PRESSED");
//            againGoodBottomBar.setVisibility(View.GONE);
            againGoodBottomBar.setVisibility(View.INVISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);

            Collections.rotate(flashcardEntries, -1);

            term.setText(flashcardEntries.get(0).front);
            answer.setText(flashcardEntries.get(0).back);

        });

        good.setOnClickListener(view1 -> {
            System.out.println("GOOD PRESSED");
            againGoodBottomBar.setVisibility(View.INVISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);
        });



    }
}