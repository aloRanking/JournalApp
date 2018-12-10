package com.example.aloranking.journalapp.model;

public class Diaries {
    String diaryId;
    String dairyEntries;
    String diaryFeeling;
    String dairyDateAndTime;

    public Diaries() {
    }

    public Diaries(String diaryId, String dairyEntries, String diaryFeeling,  String dairyDateAndTime) {
        this.diaryId = diaryId;
        this.dairyEntries = dairyEntries;
        this.diaryFeeling = diaryFeeling;
        this.dairyDateAndTime = dairyDateAndTime;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public String getDairyEntries() {
        return dairyEntries;
    }

    public String getDiaryFeeling() {
        return diaryFeeling;
    }

    public String getDairyDateAndTime() {
        return dairyDateAndTime;
    }

}
