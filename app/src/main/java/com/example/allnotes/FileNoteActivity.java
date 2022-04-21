package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNoteActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    List<putPDF> uploadFile;
    FloatingActionButton btnCreateFile;
    putPDF putPDF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_note);
        listView = findViewById(R.id.listFile);
        btnCreateFile = findViewById(R.id.createFileFab);
        uploadFile = new ArrayList<>();

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        databaseReference = FirebaseDatabase.getInstance().getReference("upLoadPDF");
        //databaseReference.orderByKey().equalTo(key);
        retrieveFile();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                putPDF putPDF = uploadFile.get(i);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setType("application/pdf");
//              intent.setData(Uri.parse(putPDF.getUrl()));
//              startActivity(intent);
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(FileNoteActivity.this);
                myAlertBuilder.setTitle("Thao tác với File");
                myAlertBuilder.setMessage("Chọn thao tác");
                myAlertBuilder.setNeutralButton("Open",(dialogInterface, i1) ->{
                    putPDF putPDF = uploadFile.get(i);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setType("application/pdf");
                    intent.setData(Uri.parse(putPDF.getUrl()));
                    startActivity(intent);
                });

                myAlertBuilder.setPositiveButton("Share",(dialogInterface, i1) -> {
                    putPDF putPDF = uploadFile.get(i);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,putPDF.getUrl());
                    startActivity(Intent.createChooser(intent,"Share using"));

                });
                myAlertBuilder.setNegativeButton("Delete",(dialogInterface, i1) -> {
                    deleteFiles(key);

                });
                myAlertBuilder.show();

            }
        });
        btnCreateFile.setOnClickListener(view -> {
            Intent intent1 = new Intent(this,CreateFileActivity.class);
            startActivity(intent1);
        });

    }


    private void deleteFiles(String key){
        FirebaseDatabase.getInstance().getReference("upLoadPDF").getRef().getKey();

        databaseReference.removeValue();
        Toast.makeText(this, "File đã bị xóa", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), FileNoteActivity.class));
    }

    private void retrieveFile() {
        databaseReference = FirebaseDatabase.getInstance().getReference("upLoadPDF");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    putPDF putPDF = ds.getValue(com.example.allnotes.putPDF.class);
                    uploadFile.add(putPDF);
                }

                String [] uploadName = new String[uploadFile.size()];
                for (int i = 0; i < uploadName.length; i++) {
                    uploadName[i] ="File" +" "+ (i +1) +" "+  uploadFile.get(i).getName() + ".pdf";
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,uploadName){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position,convertView,parent);
                        TextView textView = (TextView)view
                                .findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        textView.setTextSize(18);
                        return view;

                    }
                };
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}