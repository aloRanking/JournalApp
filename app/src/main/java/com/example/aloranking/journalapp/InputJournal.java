package com.example.aloranking.journalapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloranking.journalapp.R;
import com.example.aloranking.journalapp.model.Diaries;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InputJournal extends AppCompatActivity {

    private EditText feelingsEdit, thoughtEdit;
    private Button saveButton;
    DatabaseReference databaseDiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_journal);
        databaseDiary = FirebaseDatabase.getInstance().getReference("dairies");

        feelingsEdit = findViewById(R.id.how_are_u_feeln_editText);
        thoughtEdit = findViewById(R.id.whats_on_ur_mind_editText);
        saveButton = findViewById(R.id.saveButton);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDairyEntry();
            }
        });
    }


    private void addDairyEntry(){

        String feeling = feelingsEdit.getText().toString().trim();
        String thoughts = thoughtEdit.getText().toString();

        SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDateandTime = databaseDateTimeFormate.format(new Date());

        if (!TextUtils.isEmpty(feeling) && !TextUtils.isEmpty(thoughts)){

            String id = databaseDiary.push().getKey();

            Diaries diary = new Diaries(id, thoughts, feeling, currentDateandTime);
            databaseDiary.child(id).setValue(diary);

            Toast.makeText(this, "Diary added", Toast.LENGTH_SHORT).show();
            finish();

        }else
            Toast.makeText(this, "you hud enter a text", Toast.LENGTH_SHORT).show();
    }
}
