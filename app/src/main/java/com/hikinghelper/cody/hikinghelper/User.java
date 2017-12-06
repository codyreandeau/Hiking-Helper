package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        image = (ImageView) findViewById(R.id.imageView4);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String name = mPreferences.getString(getString(R.string.first_name), "");
        firstName.setText(name);

        String mAge = mPreferences.getString(getString(R.string.age), "");
        age.setText(mAge);

        String exp = mPreferences.getString(getString(R.string.experience), "");
        experience.setText(exp);

        String ip = mPreferences.getString("com.hikinghelper.cody.hikinghelper.imagepath", "");
        Picasso.with(image.getContext()).load(ip).into(image);
        
        String am = mPreferences.getString(getString(R.string.about_me), "");
        aboutMe.setText(am);

        //get image from edit user info page, if any
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

                //for sending data to the edit user info activity
                Intent intent = new Intent(User.this, editUserInfo.class);
                intent.putExtra("FIRST_NAME", firstName.getText().toString());
                intent.putExtra("AGE", age.getText().toString());
                intent.putExtra("EXP", experience.getText().toString());
                intent.putExtra("ABOUT", aboutMe.getText().toString());

                //send image over to the edit user info activity as well
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

        //send user back to the home page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User.this, Home.class));
            }
        });
    }
}
