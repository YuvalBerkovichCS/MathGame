package com.example.mathproject;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {

    private List<RecordsModel> data;

    public RecordsAdapter(List<RecordsModel> data) {
        this.data = data;
        // Sort the data list by score in descending order
        Collections.sort(data, new Comparator<RecordsModel>() {
            @Override
            public int compare(RecordsModel record1, RecordsModel record2) {
                // Compare scores in reverse order
                return Double.compare(record2.getScore(), record1.getScore());
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecordsModel record = data.get(position);
        String score = String.valueOf(record.getScore());
        String dateAndTime = record.getDateAndTime();
        String difficulty = String.valueOf(record.getDifficulty());
        String cals = record.getCals();

        holder.setData(holder.itemView.getContext(), score, dateAndTime,difficulty);

        // Set the image based on the value of 'cals'
        ImageView imageCategory = holder.itemView.findViewById(R.id.image_category);
        if (cals.equals("+")) {
            imageCategory.setImageResource(R.drawable.addition);
        } else if (cals.equals("-")) {
            imageCategory.setImageResource(R.drawable.subtraction);
        } else if (cals.equals("*")) {
            imageCategory.setImageResource(R.drawable.multiplication);
        } else if (cals.equals("/")) {
            imageCategory.setImageResource(R.drawable.division);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Score;
        TextView dateAndTime;
        TextView Difficulty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Score = itemView.findViewById(R.id.score);
            dateAndTime = itemView.findViewById(R.id.date_and_time);
            Difficulty= itemView.findViewById(R.id.difficulty);
        }

        public void setData(Context context, String score, String date, String difficulty) {
            Resources resources = context.getResources();

            String scoreText = resources.getString(R.string.score_label, score);
            String dateText = resources.getString(R.string.date_label, date);

            // Get the localized difficulty string based on system language
            String difficultyText;
            switch (difficulty) {
                case "Easy":
                    difficultyText = resources.getString(R.string.difficulty_label, resources.getString(R.string.difficulty_easy));
                    break;
                case "Medium":
                    difficultyText = resources.getString(R.string.difficulty_label, resources.getString(R.string.difficulty_medium));
                    break;
                case "Hard":
                    difficultyText = resources.getString(R.string.difficulty_label, resources.getString(R.string.difficulty_hard));
                    break;
                default:
                    difficultyText = difficulty; // Use the provided value if no localized string is available
                    break;
            }

            Score.setText(scoreText);
            dateAndTime.setText(dateText);
            Difficulty.setText(difficultyText);
        }

    }

}
