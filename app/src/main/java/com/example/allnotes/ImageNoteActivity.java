package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageNoteActivity extends AppCompatActivity {
    private RecyclerView listImage;
    private ArrayList<ImageModel> list;
    private ImageAdapter adapter;
    DatabaseReference databaseReference;
    FloatingActionButton btnCreateImageNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_note);
        listImage = findViewById(R.id.listImage);
        databaseReference = FirebaseDatabase.getInstance().getReference("Image");

        btnCreateImageNote = findViewById(R.id.createImageNote);
        listImage.setHasFixedSize(true);
        listImage.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ImageAdapter(this,list);
        btnCreateImageNote.setOnClickListener(view -> startActivity(new Intent(ImageNoteActivity.this,UploadImageActivity.class)));
        showAllImage();
//        root.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()){
//                    ImageModel model = ds.getValue(ImageModel.class);
//                    list.add(model);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void showAllImage() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ImageModel model = dataSnapshot.getValue(com.example.allnotes.ImageModel.class);
                    list.add(model);
                }
                listImage.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}