package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class User extends AppCompatActivity {

    TextView firstName, age, experience, aboutMe;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = (TextView) findViewById(R.id.txtName);
        age = (TextView) findViewById(R.id.txtAge);
        experience = (TextView) findViewById(R.id.txtExp);
        aboutMe = (TextView) findViewById(R.id.txtAbout);
        image = (ImageView) findViewById(R.id.imageView2);


        firstName.setText(getIntent().getStringExtra("FIRST_NAME"));
        age.setText(getIntent().getStringExtra("AGE"));
        experience.setText(getIntent().getStringExtra("EXP"));
        aboutMe.setText(getIntent().getStringExtra("ABOUT"));

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Bitmap bmp = extras.getParcelable("imagebitmap");
            image.setImageBitmap(bmp);
        }

        //set action bar text
        getSupportActionBar().setTitle("Your Profile");

        Button btnUpdate=(Button)findViewById(R.id.btnUpdate);

        //Send user to Update User Info Page page
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(User.this, editUserInfo.class);
                intent.putExtra("FIRST_NAME", firstName.getText().toString());
                intent.putExtra("AGE", age.getText().toString());
                intent.putExtra("EXP", experience.getText().toString());
                intent.putExtra("ABOUT", aboutMe.getText().toString());

                if (image != null) {
                    image.buildDrawingCache();
                    Bitmap image2 = image.getDrawingCache();
                    Bundle extras2 = new Bundle();
                    extras2.putParcelable("imagebitmap", image2);
                    intent.putExtras(extras2);
                }
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User.this, Home.class));
            }
        });
    }
}
