package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NoteDetailsActivity extends AppCompatActivity {
    FloatingActionButton btnEditTextNoteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        TextView titleOfNoteDetail = findViewById(R.id.titleTextDetailNote);
        TextView contentOfNoteDetail = findViewById(R.id.contentTextNoteDetail);
        btnEditTextNoteDetail = findViewById(R.id.EditTextNote);
        Toolbar toolbar = findViewById(R.id.toolBarOfNoteTextDetail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        btnEditTextNoteDetail.setOnClickListener(view -> {
            Intent intent1 = new Intent(view.getContext(),editTextNoteActivity.class);
            intent1.putExtra("title",intent.getStringExtra("title"));
            intent1.putExtra("content",intent.getStringExtra("content"));
            intent1.putExtra("nodeId",intent.getStringExtra("noteId"));
            view.getContext().startActivity(intent1);
        });
        contentOfNoteDetail.setText(intent.getStringExtra("content"));
        titleOfNoteDetail.setText(intent.getStringExtra("title"));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(NoteDetailsActivity.this, NoteTextActivity.class));
    }
}