package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class CreateTextNoteActivity extends AppCompatActivity {
    EditText mcreateTextTitleNote,mcreateTextContentNote;
    FloatingActionButton msaveTextNote;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore firestore;
    ProgressBar mprogressbarofcreatenote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_text_note);
        msaveTextNote = findViewById(R.id.saveTextNote);
        mcreateTextTitleNote = findViewById(R.id.createTitleText);
        mcreateTextContentNote = findViewById(R.id.creatTextContent);
        Toolbar toolbar = findViewById(R.id.toolBarCreateTextNote);
        mprogressbarofcreatenote = findViewById(R.id.progressbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
       firestore = FirebaseFirestore.getInstance();
       mUser = FirebaseAuth.getInstance().getCurrentUser();

       msaveTextNote.setOnClickListener(view -> {
           String title = mcreateTextTitleNote.getText().toString();
           String content = mcreateTextContentNote.getText().toString();
           if(title.isEmpty() || content.isEmpty()){
               Toast.makeText(this, "Nhập cả hai trường", Toast.LENGTH_SHORT).show();
           }else{
               mprogressbarofcreatenote.setVisibility(View.VISIBLE);
               DocumentReference documentReference = firestore.collection("notes").document(mUser.getUid()).collection("myNotes").document();
               Map<String,Object> textNote = new HashMap<>();
               textNote.put("title",title);
               textNote.put("content",content);
               documentReference.set(textNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       Toast.makeText(CreateTextNoteActivity.this, "Note create sucessfull", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(CreateTextNoteActivity.this,NoteTextActivity.class));
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(CreateTextNoteActivity.this, "Failed to create note", Toast.LENGTH_SHORT).show();
                       mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                   }
               });

           }
       });
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
//        super.onBackPressed();
        finish();
        startActivity(new Intent(CreateTextNoteActivity.this, NoteTextActivity.class));
    }


}