package com.example.allnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NoteDrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_draw);
    }

    public void addNewDraw(View view) {
        Intent intent = new Intent(this,DrawActivity.class);
        startActivity(intent);
    }
}