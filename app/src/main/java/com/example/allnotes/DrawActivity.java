package com.example.allnotes;

import static com.example.allnotes.PaintView.colorLits;
import static com.example.allnotes.PaintView.pathList;
import static com.example.allnotes.PaintView.current_brush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class DrawActivity extends AppCompatActivity {
    public static Path path = new Path();
    public static Paint paint_brush = new Paint();
    private Button pencil,eraser,redColor,yellowColor,greenColor,blueColor,btnSave;
    private int STORAGE_PERMISSION_CODE = 1;
    private ConstraintLayout paintView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        getSupportActionBar().hide();
        pencil = findViewById(R.id.pencil);
        eraser = findViewById(R.id.eraser);
        redColor = findViewById(R.id.redColor);
        yellowColor = findViewById(R.id.yellowColor);
        greenColor = findViewById(R.id.greenColor);
        blueColor = findViewById(R.id.blueColor);
        btnSave = findViewById(R.id.btnSave);
        paintView = findViewById(R.id.constraintLayout1);
        pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                paint_brush.setColor(Color.BLACK);
                currentColor(paint_brush.getColor());
            }
        });
        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathList.clear();
                colorLits.clear();
                path.reset();
                // Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        redColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint_brush.setColor(Color.RED);
                currentColor(paint_brush.getColor());
                // Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        yellowColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint_brush.setColor(Color.YELLOW);
                currentColor(paint_brush.getColor());
                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        greenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint_brush.setColor(Color.GREEN);
                currentColor(paint_brush.getColor());
            }
        });

        blueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paint_brush.setColor(Color.BLUE);
                currentColor(paint_brush.getColor());
            }
        });
    }
    public void currentColor(int color){
        current_brush = color;
        path = new Path();
    }
}