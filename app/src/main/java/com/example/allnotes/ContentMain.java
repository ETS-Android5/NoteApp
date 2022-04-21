package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ContentMain extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        getSupportActionBar().setTitle("All Notes");
        mAuth = FirebaseAuth.getInstance();



    }

    public void ClickDraw(View view) {
        Intent intent = new Intent(this,DrawActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        switch (item.getItemId()){
            case R.id.logout:

                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));



        }
        return super.onOptionsItemSelected(item);
    }


    public void ImgTextNote(View view) {
        startActivity(new Intent(ContentMain.this,NoteTextActivity.class));
    }

    public void ClickPDFFile(View view) {
        startActivity(new Intent(ContentMain.this,FileNoteActivity.class));
    }
}