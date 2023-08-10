package com.example.mathproject;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mathproject.databinding.LoginFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private LoginFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        progressBar = binding.progressBar;


        binding.btnAskHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","TheProject@email.com", null));
                startActivity(Intent.createChooser(intent, getString(R.string.choose_email)));

            }
        });


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin(view);
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        return view;
    }




    private void userLogin(View view) {

        String username = binding.username.getText().toString().trim();
        String password = binding.password.getText().toString().trim();


        if (username.isEmpty()){
            binding.username.setError(getString(R.string.enter_valid_user));
            binding.username.requestFocus();
            return;
        }

        if (password.isEmpty()){
            binding.password.setError(getString(R.string.pass_require));
            binding.password.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    binding.username.setText("");
                    binding.password.setText("");
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_playFragment);
                    Toast.makeText(getActivity(), R.string.login_suc, LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getActivity(), R.string.login_fail, LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }



    public void onDestroyView() {
        super.onDestroyView();
    }
}