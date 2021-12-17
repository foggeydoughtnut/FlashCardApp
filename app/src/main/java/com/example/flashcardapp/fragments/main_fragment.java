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
    private TextView term;
    private TextView answer;
    private ObservableArrayList<FlashCardEntry> flashcardEntries;
    private int status0;
    private int status1;
    private int status2;

    public main_fragment() {
        super(R.layout.fragment_main_fragment);

    }

    private void getStatusSizes(){
        status0 = 0;
        status1 = 0;
        status2 = 0;
        flashcardEntries.forEach(item -> {
            switch(item.status){
                case 0:
                    status0 += 1;
                    break;
                case 1:
                    status1 += 1;
                    break;
                case 2:
                    status2 += 1;
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FlashCardViewModel viewModel = new ViewModelProvider(getActivity()).get(FlashCardViewModel.class);

        try {
            viewModel.doneLoading.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        viewModel.refresh();


        try {
            viewModel.doneLoading.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flashcardEntries = viewModel.getEntries();
        getStatusSizes();
        term  = view.findViewById(R.id.Term);
        answer = view.findViewById(R.id.Answer);
        Collections.shuffle(flashcardEntries);
        if (!flashcardEntries.isEmpty()){
            term.setText(flashcardEntries.get(0).front);
            answer.setText(flashcardEntries.get(0).back);
        }


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

            Bundle arguments = new Bundle();
            arguments.putLong("id", flashcardEntries.get(0).id);


            fragManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_contatiner, edit_fragment.class, arguments)
                    .commit();



        });

        // CARD LOGIC
        LinearLayout card = view.findViewById(R.id.Card);
        LinearLayout bottomBar = view.findViewById(R.id.bottomBar);
        LinearLayout againGoodBottomBar = view.findViewById(R.id.AgainGoodBar);

        bottomBar.setVisibility(View.VISIBLE);



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
            if (!flashcardEntries.isEmpty()) {

                Collections.rotate(flashcardEntries, -1);

                term.setText(flashcardEntries.get(0).front);
                answer.setText(flashcardEntries.get(0).back);
                if (flashcardEntries.get(0).status != 1) {
                    if (flashcardEntries.get(0).status == 0) status0--;
                    else status2--;
                    flashcardEntries.get(0).status = 1;
                    viewModel.updateCard(flashcardEntries.get(0));
                    status1++;

                }

            }
            System.out.println("AGAIN PRESSED");
//            againGoodBottomBar.setVisibility(View.GONE);
            againGoodBottomBar.setVisibility(View.INVISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);
            updateBottomBar(view);


        });

        good.setOnClickListener(view1 -> {
            System.out.println("GOOD PRESSED");
            againGoodBottomBar.setVisibility(View.INVISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);


            if (flashcardEntries.isEmpty()) return;

            FlashCardEntry curr = flashcardEntries.get(0);

            if (curr.status != 0){
                flashcardEntries.remove(0);
                if (curr.status == 1) {
                    status1--;
                }
                else status2--;
            }
            else{
                curr.status = 1;
                status1++;
                status0--;
            }

                viewModel.studiedCard(curr);

            String front = "All done for today!";
            String back = "I don't know what you're looking for here";
            if (flashcardEntries.size() != 0) {
                Collections.rotate(flashcardEntries, -1);
                front = flashcardEntries.get(0).front;
                back = flashcardEntries.get(0).back;
            }
            term.setText(front);
            answer.setText(back);

            updateBottomBar(view);
        });


        updateBottomBar(view);

    }

    private void updateBottomBar(View view) {
        TextView newCount = view.findViewById(R.id.newCardCount);
        TextView learningCount = view.findViewById(R.id.learningCardCount);
        TextView oldCardCount = view.findViewById(R.id.oldCardCount);
        newCount.setText("" + status0);
        learningCount.setText("" + status1);
        oldCardCount.setText("" + status2);



    }
}