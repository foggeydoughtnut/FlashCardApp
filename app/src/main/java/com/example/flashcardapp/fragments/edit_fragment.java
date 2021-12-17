package com.example.flashcardapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.flashcardapp.R;
import com.example.flashcardapp.models.FlashCardEntry;
import com.example.flashcardapp.viewmodels.FlashCardViewModel;

import java.util.concurrent.ExecutionException;

public class edit_fragment extends Fragment {

    public edit_fragment() {
        super(R.layout.fragment_edit_fragment);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView cancelButton = view.findViewById(R.id.cancel);
        TextView updateButton = view.findViewById(R.id.Update);
        FlashCardViewModel viewModel = new ViewModelProvider(getActivity()).get(FlashCardViewModel.class);
        assert getArguments() != null;
        Long id = getArguments().getLong("id");
        FlashCardEntry flashcard = null;

        // Created this part with some help from my brother
        try {
            flashcard = viewModel.getById(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        // End

        EditText frontTextInput = view.findViewById(R.id.frontEditText);
        EditText backTextInput = view.findViewById(R.id.backEditText);
        assert flashcard != null;
        frontTextInput.setText(flashcard.front);
        backTextInput.setText(flashcard.back);



        cancelButton.setOnClickListener(nice -> {
            System.out.println("CANCEL PRESSED");

            FragmentManager fragManager = getParentFragmentManager();
            fragManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_contatiner, main_fragment.class, null)
                    .commit();
        });



        FragmentManager fragManager = getParentFragmentManager();

        FlashCardEntry finalFlashcard = flashcard;
        updateButton.setOnClickListener(nice -> {
            System.out.println("UPDATE PRESSED");

            String frontText = frontTextInput.getText().toString();
            String backText = backTextInput.getText().toString();
            finalFlashcard.front = frontText;
            finalFlashcard.back = backText;
            viewModel.updateCard(finalFlashcard);


//            viewModel.saveFlashCard(frontText, backText);

            frontTextInput.setText("");
            backTextInput.setText("");



        });

    }
}