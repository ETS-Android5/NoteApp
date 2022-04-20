package com.example.allnotes;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateTextNoteActivity extends AppCompatActivity {
    EditText mcreateTextTitleNote, mcreateTextContentNote;
    FloatingActionButton msaveTextNote;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    ProgressBar mprogressbarofcreatenote;
    ImageView iv_mic;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_note);

        msaveTextNote = findViewById(R.id.saveTextNote);
        mcreateTextTitleNote = findViewById(R.id.createTitleText);
        mcreateTextContentNote = findViewById(R.id.creatTextContent);
        Toolbar toolbar = findViewById(R.id.toolBarCreateTextNote);
        mprogressbarofcreatenote = findViewById(R.id.progressbarofcreatenote);

        iv_mic = findViewById(R.id.iv_mic);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        msaveTextNote.setOnClickListener(view -> {
            String title = mcreateTextTitleNote.getText().toString();
            String content = mcreateTextContentNote.getText().toString();
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Nhập cả hai trường", Toast.LENGTH_SHORT).show();
            } else {
                mprogressbarofcreatenote.setVisibility(View.VISIBLE);
                DocumentReference documentReference = firestore.collection("notes").document(mUser.getUid()).collection("myNotes").document();
                Map<String, Object> textNote = new HashMap<>();
                textNote.put("title", title);
                textNote.put("content", content);
                documentReference.set(textNote).addOnSuccessListener(unused -> {
                    Toast.makeText(CreateTextNoteActivity.this, "Tạo Note thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(CreateTextNoteActivity.this, NoteTextActivity.class));
                }).addOnFailureListener(e -> {
                    Toast.makeText(CreateTextNoteActivity.this, "Tạo Note không thành công", Toast.LENGTH_SHORT).show();
                    mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                });

            }
        });
        //Try Speech to Text
        iv_mic.setOnClickListener(v -> {
            Intent intent
                    = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            } catch (Exception e) {
                Toast.makeText(CreateTextNoteActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mcreateTextContentNote.setText(Objects.requireNonNull(result).get(0));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // fix bug
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(CreateTextNoteActivity.this, NoteTextActivity.class));
    }

}