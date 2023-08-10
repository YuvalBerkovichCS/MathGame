package com.example.mathproject;

import static android.widget.Toast.*;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mathproject.databinding.RegisterFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {


    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;

    private ProgressBar progressBar;

    private RegisterFragmentBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = RegisterFragmentBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        progressBar = binding.progressBar;

        View view = binding.getRoot();

        binding.btnCreateRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(view);
            }
        });

        return view;
    }


    private void registerUser(View view) {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        String repassword = binding.repassword.getText().toString();




        if (email.isEmpty()) {
            binding.email.setError(getString(R.string.email_must));
            binding.email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.password.setError(getString(R.string.pass_must));
            binding.password.requestFocus();
            return;
        }

        if (repassword.isEmpty()) {
            binding.repassword.setError(getString(R.string.re_pass_must));
            binding.repassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.setError(getString(R.string.val_email));
            binding.email.requestFocus();
            return;
        }

        if (password.length() < 8) {
            binding.password.setError(getString(R.string.min_pass));
            binding.password.requestFocus();
            return;
        }

        if (!password.equals(repassword)) {
            binding.repassword.setError(getString(R.string.no_match_pass));
            binding.repassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                if (isNewUser) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User registration successful
                                makeText(getActivity(), R.string.reg_in_suc, LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);

                                // Create a new document in the "users" collection with the user's email
                                String uid = mAuth.getCurrentUser().getUid();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                mFirestore.collection("users").document(uid).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Document created successfully
                                                Log.d("RegisterFragment", "User document created successfully!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Document creation failed
                                                Log.e("RegisterFragment", "Failed to create user document: " + e.getMessage());
                                            }
                                        });
                            } else {
                                // User registration failed
                                makeText(getActivity(), "Failed to register, please try again later.", LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    // User already exists
                    makeText(getActivity(), R.string.email_exist, LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

}