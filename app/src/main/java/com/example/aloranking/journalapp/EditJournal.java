package com.example.aloranking.journalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aloranking.journalapp.model.Diaries;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditJournal extends AppCompatActivity {
    private EditText feelingsEdit, thoughtEdit;
    private Button saveButton;
    DatabaseReference databaseDiary;
    String diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal);


        feelingsEdit = findViewById(R.id.how_are_u_feeln_editText);
        thoughtEdit = findViewById(R.id.whats_on_ur_mind_editText);
        saveButton = findViewById(R.id.saveButton);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        String feeling = extras.getString("feeling");
        String thoughts = extras.getString("thoughts");
        diaryId = extras.getString("id");

        if (feeling != null) {
            // do something with the data
            feelingsEdit.setText(feeling);
        }

        if (thoughts != null) {
            // do something with the data
            thoughtEdit.setText(thoughts);
        }

    }


    public void saveEdit(View view) {

        String feelingsUpdate = feelingsEdit.getText().toString();
        String thoughtsUpdate = thoughtEdit.getText().toString();

        SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDateandTime = databaseDateTimeFormate.format(new Date());




        Intent i = new Intent();
       /* i.putExtra("feeling", feelingsUpdate);
        i.putExtra("thoughts", thoughtsUpdate);
        i.putExtra("time", currentDateandTime);
        i.putExtra("id", diaryId);
        //setResult(RESULT_OK, i);*/


       updateJournal(diaryId,thoughtsUpdate,feelingsUpdate,currentDateandTime);
        setResult(RESULT_OK, i);
        finish();
    }

    private boolean updateJournal(String id, String thoughtsUpdate,String feelingsUpdate, String currentDateandTime ){

        databaseDiary = FirebaseDatabase.getInstance().getReference("dairies").child(diaryId);

        Diaries mDiaries = new Diaries(id, thoughtsUpdate,feelingsUpdate,currentDateandTime);
        databaseDiary.setValue(mDiaries);

        Toast.makeText(this, "Diary updated", Toast.LENGTH_SHORT).show();

        return true;

    }
}
