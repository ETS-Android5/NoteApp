package com.example.allnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editTextNoteActivity extends AppCompatActivity {
    Intent intent;
    EditText editTextTitleOfNote,editTextContentOfNote;
    FloatingActionButton btnsaveEditText;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_note);
        editTextTitleOfNote = findViewById(R.id.editTitleText);
        editTextContentOfNote = findViewById(R.id.editTextContent);
        btnsaveEditText = findViewById(R.id.saveEditNote);
        intent = getIntent();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolBarOfEditTextNote);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btnsaveEditText.setOnClickListener(view -> {
            String newTitle = editTextTitleOfNote.getText().toString();
            String newcontent = editTextContentOfNote.getText().toString();
            if(newTitle.isEmpty() || newcontent.isEmpty()){
                Toast.makeText(this, "Tiêu đề hoặc nội dung đang để trống", Toast.LENGTH_SHORT).show();
            }else{
                DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(intent.getStringExtra("noteId"));
                Map<String,Object> note = new HashMap<>();
                note.put("title",newTitle);
                note.put("content",newcontent);
                documentReference.set(note).addOnSuccessListener(unused -> {
                    Toast.makeText(editTextNoteActivity.this, "Ghi chú đã được cập nhật", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(editTextNoteActivity.this,NoteTextActivity.class));
                }).addOnFailureListener(e -> Toast.makeText(editTextNoteActivity.this, "Ghi chú cập nhật không thành công", Toast.LENGTH_SHORT).show());
            }

        });

        String noteTitle =  intent.getStringExtra("title");
        String notecontent = intent.getStringExtra("content");
        editTextContentOfNote .setText(notecontent);
        editTextTitleOfNote.setText(noteTitle);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(editTextNoteActivity.this, NoteTextActivity.class));
    }
}