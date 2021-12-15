package com.example.flashcardapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.flashcardapp.R;
import com.example.flashcardapp.viewmodels.FlashCardViewModel;

public class add_fragment extends Fragment {


    public add_fragment() {
        super(R.layout.fragment_add_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        TextView cancelButton = view.findViewById(R.id.Cancel);
        TextView saveButton = view.findViewById(R.id.Save);
        FlashCardViewModel viewModel = new ViewModelProvider(getActivity()).get(FlashCardViewModel.class);


        cancelButton.setOnClickListener(nice -> {
            System.out.println("CANCEL PRESSED");

            FragmentManager fragManager = getParentFragmentManager();
            fragManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_contatiner, main_fragment.class, null)
                    .commit();
        });



        saveButton.setOnClickListener(nice -> {
            System.out.println("SAVE PRESSED");
            EditText frontTextInput = view.findViewById(R.id.FrontEditText);
            EditText backTextInput = view.findViewById(R.id.BackEditText);
            System.out.printf("%s, %s\n", frontTextInput.getText().toString(), backTextInput.getText().toString());
            String frontText = frontTextInput.getText().toString();
            String backText = backTextInput.getText().toString();

            viewModel.saveFlashCard(frontText, backText);

            frontTextInput.setText("");
            backTextInput.setText("");


        });
    }
}