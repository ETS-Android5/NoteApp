package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editTextNoteActivity extends AppCompatActivity {
    Intent intent;
    EditText editTextTitleOfNote,editTextContentOfNote;
    FloatingActionButton btnsaveEditText;
    FirebaseAuth mAuth;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnsaveEditText.setOnClickListener(view -> {
            String newTitle = editTextTitleOfNote.getText().toString();
            String newcontent = editTextContentOfNote.getText().toString();
            if(newTitle.isEmpty() || newcontent.isEmpty()){
                Toast.makeText(this, "Tiêu đề hoặc nội dung đang để trống", Toast.LENGTH_SHORT).show();
                return;
            }else{
                DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(intent.getStringExtra("noteId"));
                Map<String,Object> note = new HashMap<>();
                note.put("title",newTitle);
                note.put("content",newcontent);
                documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(editTextNoteActivity.this, "Ghi chú đã được cập nhật", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editTextNoteActivity.this,NoteTextActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editTextNoteActivity.this, "Ghi chú cập nhật không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        String noteTitle =  intent.getStringExtra("title");
        String notecontent = intent.getStringExtra("content");
        editTextContentOfNote .setText(notecontent);
        editTextTitleOfNote.setText(noteTitle);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:

                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));



        }
        return super.onOptionsItemSelected(item);
    }
}