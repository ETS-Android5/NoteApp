package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageNoteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView listImage;
    private ArrayList<ImageModel> list;
    private ImageAdapter adapter;
    DatabaseReference databaseReference;
    FloatingActionButton btnCreateImageNote;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_note);
        listImage = findViewById(R.id.listImage);
        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        mAuth = FirebaseAuth.getInstance();
        btnCreateImageNote = findViewById(R.id.createImageNote);
        drawerLayout = findViewById(R.id.draw_layout_image);
        navigationView = findViewById(R.id.nav_view_image);
        toolbar = findViewById(R.id.toolbar_image);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(ImageNoteActivity.this, ContentMain.class));
                break;
            case R.id.nav_draw:
                startActivity(new Intent(ImageNoteActivity.this, DrawActivity.class));
                break;
            case R.id.nav_image:
                startActivity(new Intent(ImageNoteActivity.this, ImageNoteActivity.class));
                break;
            case R.id.nav_file:
                Intent intent = new Intent(ImageNoteActivity.this, FileNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_text:
                startActivity(new Intent(ImageNoteActivity.this,NoteTextActivity.class));
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}