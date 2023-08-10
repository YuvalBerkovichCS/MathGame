package com.example.mathproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.mathproject.databinding.PlayFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

import android.media.MediaPlayer;

public class PlayFragment extends Fragment {

    private ImageView addition;
    private ImageView subtraction;
    private ImageView multiplication;
    private ImageView division;
    private PlayFragmentBinding binding;
    private String selectedOperator = "+"; // Default operator


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PlayFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        addition = view.findViewById(R.id.addition);
        subtraction = view.findViewById(R.id.subtraction);
        multiplication = view.findViewById(R.id.multiplication);
        division = view.findViewById(R.id.division);


        binding.webSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.mathplayground.com/math-games.html";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperator = "+";
                showDifficultyDialog(selectedOperator);
            }
        });

        subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperator = "-";
                showDifficultyDialog(selectedOperator);
            }
        });

        multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperator = "*";
                showDifficultyDialog(selectedOperator);
            }
        });

        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOperator = "/";
                showDifficultyDialog(selectedOperator);
            }
        });

        binding.btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_playFragment_to_records);
            }
        });

        return view;
    }


    private void showDifficultyDialog(final String selectedOperator) {
        final String[] difficultyLevels = getResources().getStringArray(R.array.difficulty_levels); // Retrieve the difficulty levels from resources

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.select_difficulty)
                .setItems(difficultyLevels, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedDifficulty = difficultyLevels[which]; // Get the selected difficulty
                        navigateToGameFragment(selectedOperator, selectedDifficulty);
                    }
                });
        builder.create().show();
    }

    private void navigateToGameFragment(String selectedOperator, String selectedDifficulty) {
        Bundle bundle = new Bundle();
        bundle.putString("operator", selectedOperator);
        bundle.putString("difficulty", selectedDifficulty);
        Navigation.findNavController(requireView()).navigate(R.id.action_playFragment_to_gameFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}