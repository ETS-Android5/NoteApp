package com.example.allnotes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteTextActivity extends AppCompatActivity {
    FloatingActionButton mcreatenotefab;
    RecyclerView mrecyclerView;
    private FirebaseAuth mAuth;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<TextModal,NoteViewHolder> textNodeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_text);
        mcreatenotefab = findViewById(R.id.createNoteFab);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
//        getSupportActionBar().setTitle("All Notes");
        mcreatenotefab.setOnClickListener(view -> startActivity(new Intent(NoteTextActivity.this,CreateTextNoteActivity.class)));
        Query query = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TextModal> alluserNotes = new FirestoreRecyclerOptions.Builder<TextModal>().setQuery(query,TextModal.class).build();
        textNodeAdapter = new FirestoreRecyclerAdapter<TextModal, NoteViewHolder>(alluserNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position, @NonNull TextModal textModal) {
               // Color
                int colorcode = getRandomColor();
                //
                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.btnMenupop);
                noteViewHolder.linearLayout.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorcode,null));

                noteViewHolder.noteTitle.setText(textModal.getTitle());
                noteViewHolder.noteContent.setText(textModal.getContent());
                String docId =textNodeAdapter.getSnapshots().getSnapshot(position).getId();
                noteViewHolder.itemView.setOnClickListener(view -> {
                    // detail note
                    Intent intent = new Intent(view.getContext(),NoteDetailsActivity.class);
                    intent.putExtra("title",textModal.getTitle());
                    intent.putExtra("content",textModal.getContent());
                    intent.putExtra("noteId",docId);

                    view.getContext().startActivity(intent);

                });
                popupbutton.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                    popupMenu.setGravity(Gravity.END);
                    popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(menuItem -> {
                        Intent intent = new Intent(view.getContext(),editTextNoteActivity.class);
                        intent.putExtra("title",textModal.getTitle());
                        intent.putExtra("content",textModal.getContent());
                        intent.putExtra("noteId",docId);
                        view.getContext().startActivity(intent);
                        return false;
                    });
                    popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(menuItem -> {
                       // Toast.makeText(NoteTextActivity.this, "Ghi chú đã xóa thành công", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference = firestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                        documentReference.delete().addOnSuccessListener(unused -> Toast.makeText(NoteTextActivity.this, "Ghi chú đã bị xóa", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(NoteTextActivity.this, "Xóa ghi chú không thành công", Toast.LENGTH_SHORT).show());
                        return false;
                    });
                    popupMenu.show();

                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_note,parent,false);
                return new NoteViewHolder(view);
            }
        };
        mrecyclerView = findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(textNodeAdapter);
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private TextView noteTitle;
        private TextView noteContent;
        LinearLayout linearLayout;

        public NoteViewHolder (@NonNull View itemView){
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            linearLayout = itemView.findViewById(R.id.note);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart(){
        super.onStart();
        textNodeAdapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();

        if(textNodeAdapter!= null){
            textNodeAdapter.stopListening();
        }
    }
    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.green);
        colorcode.add(R.color.violet);
        colorcode.add(R.color.blue);
        colorcode.add(R.color.yellow);
        colorcode.add(R.color.lightblue);
        colorcode.add(R.color.deeppurple);
        colorcode.add(R.color.pink);
        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }

}