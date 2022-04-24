package com.example.allnotes;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class UploadImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressBar progressBarloadImg;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private boolean isClickUp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imageView = findViewById(R.id.img);
        Button btnUploadImg = findViewById(R.id.uploadImg);
        progressBarloadImg = findViewById(R.id.loadImg);
      //  Toolbar toolbar = findViewById(R.id.toolBarCreateImage);
        //setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        imageView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,2);

        });
        progressBarloadImg.setVisibility(View.INVISIBLE);
        btnUploadImg.setOnClickListener(view -> {
            if(isClickUp) {
                Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
                isClickUp = false;
            }else{
                if (imageUri != null) {
                    uploadImgToFirebase(imageUri);
                } else {
                    Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                }
                isClickUp = true;
            }
        });
    }

    private void uploadImgToFirebase(Uri uri) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + " . " + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
            ImageModel imageModel = new ImageModel(uri1.toString());
            String modelId = root.push().getKey();
            root.child(modelId).setValue(imageModel);
            progressBarloadImg.setVisibility(View.INVISIBLE);
            Toast.makeText(UploadImageActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.add_image);
            startActivity(new Intent(UploadImageActivity.this,ImageNoteActivity.class));
        })).addOnProgressListener(snapshot -> progressBarloadImg.setVisibility(View.VISIBLE)).addOnFailureListener(e -> {
            progressBarloadImg.setVisibility(View.INVISIBLE);
            Toast.makeText(UploadImageActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getFileExtension(Uri mUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));
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
        startActivity(new Intent(UploadImageActivity.this, ImageNoteActivity.class));
    }
}