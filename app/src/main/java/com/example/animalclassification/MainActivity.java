package com.example.animalclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.animalclassification.database.AnimalContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ImageView aquaticView = findViewById(R.id.aquatic);
        aquaticView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimalList.class);
                Uri uri = Uri.parse(AnimalContract.AnimalEntry.CONTENT_URI+"/Aquatic");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        ImageView mammals = findViewById(R.id.mammals);
        mammals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimalList.class);
                Uri uri = Uri.parse(AnimalContract.AnimalEntry.CONTENT_URI+"/Mammals");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        ImageView birds = findViewById(R.id.birds);
        birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimalList.class);
                Uri uri = Uri.parse(AnimalContract.AnimalEntry.CONTENT_URI+"/Birds");
                intent.setData(uri);
                startActivity(intent);
            }
        });

        ImageView reptiles = findViewById(R.id.reptiles);
        reptiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimalList.class);
                Uri uri = Uri.parse(AnimalContract.AnimalEntry.CONTENT_URI+"/Reptiles");
                intent.setData(uri);
                startActivity(intent);
            }
        });
    }



}