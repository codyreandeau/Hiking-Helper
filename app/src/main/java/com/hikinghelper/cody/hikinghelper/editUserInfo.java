package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class editUserInfo extends AppCompatActivity {

    EditText firstName, age, experience, aboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = (EditText) findViewById(R.id.txtName);
        age = (EditText) findViewById(R.id.txtAge);
        experience = (EditText) findViewById(R.id.txtExp);
        aboutMe = (EditText) findViewById(R.id.txtAbout);

        firstName.setText(getIntent().getStringExtra("FIRST_NAME"));
        age.setText(getIntent().getStringExtra("AGE"));
        experience.setText(getIntent().getStringExtra("EXP"));
        aboutMe.setText(getIntent().getStringExtra("ABOUT"));

        if(experience.equals("EXPERIENCE")){
            experience.setText("");
        }

        if(aboutMe.equals("About Me")){
            aboutMe.setText("");
        }

        Button btnUpdate=(Button)findViewById(R.id.btnUpdate);

        //Send user to back to user page after update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(editUserInfo.this, User.class);
                intent.putExtra("FIRST_NAME", firstName.getText().toString());
                intent.putExtra("AGE", age.getText().toString());
                intent.putExtra("EXP", experience.getText().toString());
                intent.putExtra("ABOUT", aboutMe.getText().toString());
                startActivity(intent);
            }
        });

        //set action bar text
        getSupportActionBar().setTitle("Edit Your Information");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(editUserInfo.this, User.class));
            }
        });
    }

}
