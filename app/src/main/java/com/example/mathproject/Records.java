package com.example.mathproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mathproject.databinding.RecordsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Records extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecordsBinding binding;
    private boolean flag = true;
    private RecyclerView recyclerView;
    private List<RecordsModel> recordsList;
    private RecordsAdapter recordsAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecordsBinding.inflate(inflater);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        readFromFirestore();

        return view;
    }

    private String currId = "";

    private void readFromFirestore() {
        progressBar = binding.progressBar;
        recordsList = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();

            Task<QuerySnapshot> usersTask = db.collection("users").get();
            progressBar.setVisibility(View.VISIBLE);

            usersTask.addOnSuccessListener(queryDocumentSnapshots -> {
                if (flag) {
                    flag = false;
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (Objects.equals(documentSnapshot.get("email"), email)) {
                            currId = documentSnapshot.getId();
                            break;
                        }
                    }
                    readFromFirestore();
                } else {
                    Task<QuerySnapshot> scoreTask = db.collection("users").document(currId).collection("score").get();
                    scoreTask.addOnSuccessListener(queryDocumentSnapshots1 -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots1) {
                            Map<String, Object> data = documentSnapshot.getData();
                            Long scoreLong = (Long) data.get("score");
                            String dateAndTime = (String) data.get("DateAndTime");
                            String cals = (String) data.get("cals");
                            String difficulty = (String) data.get("difficulty");
                            recordsList.add(new RecordsModel(scoreLong, dateAndTime, cals, difficulty));
                        }
                        recyclerView = binding.recyclerViewRecords;
                        layoutManager = new LinearLayoutManager(getContext());
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recordsAdapter = new RecordsAdapter(recordsList);
                        recyclerView.setAdapter(recordsAdapter);
                        recordsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    });
                }
            });
        }
    }
}
