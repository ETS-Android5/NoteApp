package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button btnUploadImg,btnShowAllImg;
    private ProgressBar progressBarloadImg;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        imageView = findViewById(R.id.img);
        btnUploadImg = findViewById(R.id.uploadImg);
        btnShowAllImg = findViewById(R.id.showAllImg);
        progressBarloadImg = findViewById(R.id.loadImg);
        imageView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent,2);

        });
        progressBarloadImg.setVisibility(View.INVISIBLE);
        btnUploadImg.setOnClickListener(view -> {
            if(imageUri != null){
                uploadImgToFirebase(imageUri);
            }else {
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImgToFirebase(Uri uri) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + " . " + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageModel imageModel = new ImageModel(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(imageModel);
                        progressBarloadImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadImageActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.add_image);
                        startActivity(new Intent(UploadImageActivity.this,ImageNoteActivity.class));
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBarloadImg.setVisibility(View.VISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBarloadImg.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadImageActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
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
}