package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ContentMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
//        getSupportActionBar().setTitle("All Notes");
        mAuth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //----------------------Toolbar-------------------------
        //setSupportActionBar(toolbar);

        //Hide or show item
        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_logout).setVisible(false);
//        menu.findItem(R.id.nav_profile).setVisible(false);

        //----------------------Navigation Drawer Menu----------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);



    }
    @Override
    public void onBackPressed() {           //=>Tránh đóng ứng dụng khi ấn nút back

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(ContentMain.this, ContentMain.class));
                break;
            case R.id.nav_draw:
                startActivity(new Intent(ContentMain.this, DrawActivity.class));
                break;
            case R.id.nav_image:
                startActivity(new Intent(ContentMain.this, ImageNoteActivity.class));
                break;
            case R.id.nav_file:
                Intent intent = new Intent(ContentMain.this, FileNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_text:
                startActivity(new Intent(ContentMain.this,NoteTextActivity.class));
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    public void ClickImageNote(View view) {
        startActivity(new Intent(ContentMain.this,ImageNoteActivity.class));
    }
}