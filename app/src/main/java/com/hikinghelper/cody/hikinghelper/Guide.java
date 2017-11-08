package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Guide extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button hike_but =(Button)findViewById(R.id.hike_but);
        Button pack_but =(Button)findViewById(R.id.pack_but);
        Button wear_but =(Button)findViewById(R.id.wear_but);
        Button tip_but =(Button)findViewById(R.id.tip_but);



        //Send user to Before_Hike page
        hike_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Guide.this, hike_but.class));
            }
        });
        //Send user to what to wear page
        wear_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Guide.this, wear_but.class));
            }
        });
        //Send user to what to pack page
        pack_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Guide.this, pack_but.class));
            }
        });
        //Send user to tip and trick page
        tip_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Guide.this, tip_but.class));
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Guide.this, Home.class));
            }
        });
    }

}
