package com.example.mathproject;


import static android.content.ContentValues.TAG;
import static android.widget.Toast.*;
import static com.example.mathproject.R.string.time_up;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class GameFragment extends Fragment {
    private TextView TimeTextView;
    private TextView QuestionTextView;
    private TextView ScoreTextView;
    private TextView AlertTextView;
    private TextView FinalScoreTextView;
    private TextView FinalPointsTextView;


    private MediaPlayer mediaPlayer;

    private TextView PointsTextView;
    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;

    private CountDownTimer countDownTimer;
    private Random random = new Random();
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private int indexOfCorrectAnswer = 0;
    private ArrayList<Integer> answers = new ArrayList<>();
    private int points = 0;
    private int score = 0;
    private int totalQuestions = 0;
    private String cals = "";
    private String difficulty = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.playThemeMusicTwo();
        }


        Bundle bundle = getArguments();
        if (bundle != null) {
            cals = bundle.getString("operator", "");
            difficulty = bundle.getString("difficulty", "");
        }


        TimeTextView = view.findViewById(R.id.TimeTextView);
        QuestionTextView = view.findViewById(R.id.QuestionTextView);
        ScoreTextView = view.findViewById(R.id.ScoreTextView);
        AlertTextView = view.findViewById(R.id.AlertTextView);
        PointsTextView = view.findViewById(R.id.PointsTextView);
        btn0 = view.findViewById(R.id.button0);
        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);


        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelect(view);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelect(view);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelect(view);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionSelect(view);
            }
        });

        start();


        return view;
    }


    public int performOperation() {
        int wrongAnswer;
        //System.out.println("this is: "+cals +" "+ difficulty);

        if (difficulty.equals("Medium") || difficulty.equals("בינוני")) {
            switch (cals) {
                case "+":
                    wrongAnswer = random.nextInt(20) + 20;
                    break;
                case "-":
                    wrongAnswer = random.nextInt(20);
                    break;
                case "*":
                    wrongAnswer = random.nextInt(300) + 100;
                    break;
                case "/":
                    wrongAnswer = random.nextInt(10) + 10;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator for Medium difficulty");
            }
        } else if (difficulty.equals("Hard") || difficulty.equals("קשה")) {
            switch (cals) {
                case "+":
                    wrongAnswer = random.nextInt(32) + 30;
                    break;
                case "-":
                    wrongAnswer = random.nextInt(30);
                    break;
                case "*":
                    wrongAnswer = random.nextInt(500) + 400;
                    break;
                case "/":
                    wrongAnswer = random.nextInt(10) + 20;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator for Hard difficulty");
            }
        } else {
            switch (cals) {
                case "+":
                    wrongAnswer = random.nextInt(20);
                    break;
                case "-":
                    wrongAnswer = random.nextInt(18);
                    break;
                case "*":
                    wrongAnswer = random.nextInt(100);
                    break;
                case "/":
                    wrongAnswer = random.nextInt(10);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operator for default difficulty");
            }
        }
        return wrongAnswer;

    }


    @SuppressLint("SetTextI18n")
    private void NextQuestion(String cal) {
        int tmp;
        if (difficulty.equals("Medium") || difficulty.equals("בינוני")) {
            a = random.nextInt(11) + 10;
            b = random.nextInt(11) + 10;
        } else if (difficulty.equals("Hard") || difficulty.equals("קשה")) {
            a = random.nextInt(11) + 20;
            b = random.nextInt(11) + 20;
        } else {
            a = random.nextInt(9) + 1;
            b = random.nextInt(9) + 1;
        }

        c = a * b;
        if (cal.equals("/")) {
            QuestionTextView.setText(c + cal + b);
        } else if (a > b) {
            QuestionTextView.setText(a + cal + b);
        } else {
            QuestionTextView.setText(b + cal + a);
        }
        if (cal.equals("-") && b > a) {
            tmp = a;
            a = b;
            b = tmp;
            QuestionTextView.setText(a + cal + b);
        }
        indexOfCorrectAnswer = random.nextInt(4);

        answers.clear();


        for (int i = 0; i < 4; i++) {


            if (indexOfCorrectAnswer == i) {
                switch (cal) {
                    case "+":
                        answers.add(a + b);
                        break;
                    case "-":
                        answers.add(a - b);
                        break;
                    case "*":
                        answers.add(a * b);
                        break;
                    case "/":
                        try {
                            answers.add(c / b);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }

            } else {
                int wrongAnswer = performOperation();

                try {
                    while (wrongAnswer == (a + b) || wrongAnswer == (a - b) || wrongAnswer == (a * b) || wrongAnswer == (c / b) || answers.contains(wrongAnswer)) {
                        wrongAnswer = performOperation();

                    }
                    answers.add(wrongAnswer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            btn0.setText(String.valueOf(answers.get(0)));
            btn1.setText(String.valueOf(answers.get(1)));
            btn2.setText(String.valueOf(answers.get(2)));
            btn3.setText(String.valueOf(answers.get(3)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void optionSelect(View view) {
        totalQuestions++;
        if (String.valueOf(indexOfCorrectAnswer).equals(view.getTag().toString())) {
            points++;
            score++;
            AlertTextView.setText(R.string.correct);
        } else {
            AlertTextView.setText(R.string.wrong);
            if (score > 0) {
                score--;
            }
        }

        PointsTextView.setText(String.valueOf(score));
        ScoreTextView.setText(points + "/" + totalQuestions);
        NextQuestion(cals);
    }


    public void PlayAgain(View view) {
        points = 0;
        totalQuestions = 0;
        score = 0;
        PointsTextView.setText(String.valueOf(score));
        ScoreTextView.setText(points + "/" + totalQuestions);
        countDownTimer.start();
    }

    private void start() {
        NextQuestion(cals);
        countDownTimer = new CountDownTimer(20000, 1000) {
            public void onTick(long p0) {
                TimeTextView.setText((p0 / 1000) + " " + getString(R.string.seconds));
            }

            public void onFinish() {
                TimeTextView.setText(time_up);
                openDialog();
            }
        }.start();
    }

    private AlertDialog showDialog;

    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View winDialog = inflater.inflate(R.layout.win_layout, null);
        FinalScoreTextView = winDialog.findViewById(R.id.FinalScoreTextView);
        FinalPointsTextView = winDialog.findViewById(R.id.FinalPointsTextView);
        Button btnPlayAgain = winDialog.findViewById(R.id.buttonPlayAgain);
        Button btnBack = winDialog.findViewById(R.id.btnBack);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setCancelable(false);
        dialog.setView(winDialog);
        FinalScoreTextView.setText(points + "/" + totalQuestions);
        FinalPointsTextView.setText(String.valueOf(score));

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View it) {
                showDialog.dismiss();
                PlayAgain(it);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (showDialog != null) {
            showDialog.dismiss();
        }

        if (!getActivity().isFinishing()) {
            showDialog = dialog.create();
            showDialog.show();
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Get the current user
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Jerusalem");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        String currentDateAndTime = sdf.format(new Date());

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Create a new collection document for the score
            DocumentReference scoreRef = db.collection("users").document(uid).collection("score").document();

            // Set the time zone to Jerusalem


            if (difficulty.equals("בינוני") || difficulty.equals("Medium")) {
                difficulty = "Medium";
            } else if (difficulty.equals("קשה") || difficulty.equals("Hard")) {
                difficulty = "Hard";
            } else {
                difficulty = "Easy";
            }


            // Set the score value and date/time within the score document
            scoreRef.set(new HashMap<String, Object>() {{
                        put("score", score);
                        put("DateAndTime", currentDateAndTime);
                        put("cals", cals);
                        put("difficulty", difficulty);


                    }})
                    .addOnSuccessListener(aVoid -> {
                        // Score document created successfully
                        makeText(getContext(), R.string.score_update, LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failed to create the score document
                        makeText(getContext(), R.string.failed_score, LENGTH_SHORT).show();
                        Log.e(TAG, "Error updating score", e);
                    });
        } else {
            // No current user
            makeText(getContext(), R.string.no_user, LENGTH_SHORT).show();
        }

        Button btnShare = winDialog.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Capture the screen and share the score
                captureAndShareScore();
            }
        });

    }

    private void captureAndShareScore() {
        // Capture the game over dialog view
        View dialogView = showDialog.getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap bitmap = Bitmap.createBitmap(dialogView.getWidth(), dialogView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        dialogView.draw(canvas);

        // Save the captured bitmap to a file
        String imagePath = saveBitmapToFile(bitmap).toString();

        // Create an intent to share the score
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My score is: " + score);

        // Check if WhatsApp is installed on the device
        boolean isWhatsAppInstalled = isPackageInstalled("com.whatsapp");

        if (isWhatsAppInstalled) {
            // WhatsApp is installed, add WhatsApp-specific intent
            shareIntent.setPackage("com.whatsapp");
        }

        startActivity(Intent.createChooser(shareIntent, "Share Score"));
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager packageManager = requireContext().getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Uri saveBitmapToFile(Bitmap bitmap) {
        Uri imageUri = null;
        try {
            File imagePath = new File(requireContext().getFilesDir(), "game_over_capture.png");
            FileOutputStream outputStream = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            imageUri = FileProvider.getUriForFile(requireContext(), "com.example.mathproject.fileprovider", imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }

    public void onBackPressed() {
        if (showDialog != null && showDialog.isShowing()) {
            showDialog.dismiss();
        }
        Navigation.findNavController(requireView()).navigateUp();
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.playThemeMusic(); // Resume playing theme_music
        }
        releaseMediaPlayer();

    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}



