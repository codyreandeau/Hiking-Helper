package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class whattowear extends AppCompatActivity {

    private ImageView image1, image2, image3, image4, image5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whattowear);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //set action bar text
        //getSupportActionBar().setTitle("What to Wear");

        image1 = (ImageView)findViewById(R.id.imageView12);
        image2 = (ImageView)findViewById(R.id.imageView13);
        image3 = (ImageView)findViewById(R.id.imageView10);
        image4 = (ImageView)findViewById(R.id.imageView11);
        image5 = (ImageView)findViewById(R.id.imageView8);

        image1.setImageDrawable(getResources().getDrawable(R.drawable.wear1));
        image2.setImageDrawable(getResources().getDrawable(R.drawable.wear2));
        image3.setImageDrawable(getResources().getDrawable(R.drawable.wear3));
        image4.setImageDrawable(getResources().getDrawable(R.drawable.wear4));
        image5.setImageDrawable(getResources().getDrawable(R.drawable.wear5));

        //Send User back to guide page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(whattowear.this, Guide.class));
            }
        });
    }

}
