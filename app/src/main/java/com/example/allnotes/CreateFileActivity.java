package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class CreateFileActivity extends AppCompatActivity {
    EditText meditText;
    Button btnUploadFile,btnChooseFile;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_file);
        meditText = findViewById(R.id.editFile);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        Toolbar toolbar = findViewById(R.id.toolBarCreateFile);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("upLoadPDF");
        btnUploadFile.setEnabled(false);
        btnChooseFile.setOnClickListener(view -> {
            selectPDF();
        });


    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12 && resultCode== RESULT_OK && data != null &&data.getData() != null){
            btnUploadFile.setEnabled(true);
            meditText.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/")+ 1));
            btnUploadFile.setOnClickListener(view -> upLoadPDFFilebase(data.getData()));
        }
    }

    private void upLoadPDFFilebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading ......");
        progressDialog.show();
        StorageReference reference = storageReference.child(meditText.getText().toString() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri uri = uriTask.getResult();
                    putPDF putPDF = new putPDF(meditText.getText().toString(),uri.toString());
                    databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                    Toast.makeText(CreateFileActivity.this, "File Upload", Toast.LENGTH_SHORT).show();
                    progressDialog.show();
                    startActivity(new Intent(getApplicationContext(),FileNoteActivity.class));


                }).addOnProgressListener(snapshot -> {
                    double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressDialog.setMessage("File Upload"+" "+  (int)progress+ "%");


                });

    }

    public void retrieveFile(View view) {
        startActivity(new Intent(getApplicationContext(),FileNoteActivity.class));
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
        startActivity(new Intent(CreateFileActivity.this, FileNoteActivity.class));
    }
}