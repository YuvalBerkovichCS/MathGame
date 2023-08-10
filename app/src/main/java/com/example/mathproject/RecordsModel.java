package com.example.mathproject;

public class RecordsModel {
    private long score;
    private String dateAndTime;
    private String cals;
    private String difficulty;



    public RecordsModel(long score, String dateAndTime, String cals, String difficulty) {
        this.score = score;
        this.dateAndTime = dateAndTime;
        this.cals = cals;
        this.difficulty = difficulty;

    }

    public long getScore() {
        return score;
    }

    public String getCals() {
        return cals;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getDifficulty() {
        return difficulty;
    }

}

