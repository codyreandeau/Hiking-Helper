package com.hikinghelper.cody.hikinghelper;

import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editUserInfo extends AppCompatActivity {

    EditText firstName, age, experience, aboutMe;
    ImageView image;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/update_user_info.php";
    private StringRequest request;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firstName = (EditText) findViewById(R.id.txtMountainName);
        age = (EditText) findViewById(R.id.txtAge);
        experience = (EditText) findViewById(R.id.txtExp);
        aboutMe = (EditText) findViewById(R.id.txtAbout);
        image = (ImageView) findViewById(R.id.imageView3);


        //get information passed from the User page
        firstName.setText(getIntent().getStringExtra("FIRST_NAME"));
        age.setText(getIntent().getStringExtra("AGE"));
        experience.setText(getIntent().getStringExtra("EXP"));
        aboutMe.setText(getIntent().getStringExtra("ABOUT"));

        if(experience.equals("Experience")){
            experience.setText("");
        }

        if(aboutMe.equals("About Me")){
            aboutMe.setText("");
        }

        //Get image from the user page, if any
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            Bitmap bmp = extras.getParcelable("imagebitmap");
            image.setImageBitmap(bmp);
        }

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //mPreferences = getSharedPreferences("com.hikinghelper.cody.hikinghelper", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

        Button btnUpdate=(Button)findViewById(R.id.btnUpdate);

        //Send user to back to user page after update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mountainResponse();

                //Pass data to user profile page
                Intent intent = new Intent(editUserInfo.this, User.class);
                /*intent.putExtra("FIRST_NAME", firstName.getText().toString());
                intent.putExtra("AGE", age.getText().toString());
                intent.putExtra("EXP", experience.getText().toString());
                intent.putExtra("ABOUT", aboutMe.getText().toString());*/

                //Save first_name
                /*String fn = firstName.getText().toString();
                mEditor.putString(getString(R.string.first_name), fn);
                mEditor.commit();

                //Save age
                String ag = age.getText().toString();
                mEditor.putString(getString(R.string.age), ag);
                mEditor.commit();

                //Save experience
                String ex = experience.getText().toString();
                mEditor.putString(getString(R.string.experience), ex);
                mEditor.commit();

                //Save about me
                String am = aboutMe.getText().toString();
                mEditor.putString(getString(R.string.about_me), am);
                mEditor.commit();*/

                //Pass image to user profile page
                image.buildDrawingCache();
                Bitmap image2 = image.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image2);
                intent.putExtras(extras);

                startActivity(intent);
            }
        });

        //set action bar text
        getSupportActionBar().setTitle("Edit Your Information");

        //Send user back to the User page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(editUserInfo.this, User.class));
            }
        });

        image = (ImageView)findViewById(R.id.imageView3);
        button = (Button)findViewById(R.id.button);

        //Open image gallery for upload
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });}

        //Open image gallery function
        private void openGallery(){
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == PICK_IMAGE){
                imageUri=data.getData();
                image.setImageURI(imageUri);
        }
    }

    private void checkSharedPreferences() {
        String sharedName = mPreferences.getString(getString(R.string.first_name), "");
        String sharedAge = mPreferences.getString(getString(R.string.age), "");
        String sharedExp = mPreferences.getString(getString(R.string.experience), "");
        String sharedAboutMe = mPreferences.getString(getString(R.string.about_me), "");

        firstName.setText(sharedName);
        age.setText(sharedAge);
        experience.setText(sharedExp);
        aboutMe.setText(sharedAboutMe);
    }

    private void mountainResponse() {

        requestQueue = Volley.newRequestQueue(this);

        final String sharedUserName = mPreferences.getString("com.hikinghelper.cody.hikinghelper.username_save", "");

        //Validate user in the database
        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    //Get JSON Array userInfo
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("userInfo");
                    JSONObject data = jsonArray.getJSONObject(0);

                    //Save first name
                    String fn = data.getString("first_name");
                    mEditor.putString(getString(R.string.first_name), fn);
                    mEditor.commit();

                    //Save age
                    String ag = data.getString("age");
                    mEditor.putString(getString(R.string.age), ag);
                    mEditor.commit();

                    //Save experience
                    String exp = data.getString("experience");
                    mEditor.putString(getString(R.string.experience), exp);
                    mEditor.commit();

                    //Save about me
                    String am = data.getString("about_me");
                    mEditor.putString(getString(R.string.about_me), am);
                    mEditor.commit();

                    Toast.makeText(getApplicationContext(), "Info has been updated!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("first_name", firstName.getText().toString());
                hashMap.put("age", age.getText().toString());
                hashMap.put("experience", experience.getText().toString());
                hashMap.put("about_me", aboutMe.getText().toString());
                hashMap.put("username", sharedUserName);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
}
