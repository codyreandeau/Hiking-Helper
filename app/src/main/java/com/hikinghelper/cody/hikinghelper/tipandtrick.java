package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class tipandtrick extends AppCompatActivity {

    private ImageView image1, image2, image3, image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipandtrick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        image1 = (ImageView)findViewById(R.id.imageView12);
        image2 = (ImageView)findViewById(R.id.imageView13);
        image3 = (ImageView)findViewById(R.id.imageView10);
        image4 = (ImageView)findViewById(R.id.imageView11);

        image1.setImageDrawable(getResources().getDrawable(R.drawable.tip2));
        image2.setImageDrawable(getResources().getDrawable(R.drawable.tip3));
        image3.setImageDrawable(getResources().getDrawable(R.drawable.tip1));
        image4.setImageDrawable(getResources().getDrawable(R.drawable.tip4));

        //set action bar text
        getSupportActionBar().setTitle("Tips and Tricks");

        //Send User back to guide page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(tipandtrick.this, Guide.class));
            }
        });
    }

}
