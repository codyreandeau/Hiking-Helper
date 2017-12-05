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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class editUserInfo extends AppCompatActivity {

    EditText firstName, age, experience, aboutMe;
    ImageView image;
    Button button;
    Uri imageUri;
    boolean imageChanged = false;
    private Bitmap bitmap1;
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

        firstName = (EditText) findViewById(R.id.txtName);
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

                //Save first_name
                String fn = firstName.getText().toString();
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
                mEditor.commit();

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
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"), 999);
            }
        });}

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == 999 && resultCode == RESULT_OK && data != null){
                imageUri=data.getData();
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }catch(IOException e){
                    e.printStackTrace();
                }
                image.setImageURI(imageUri);
                imageChanged = true;
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

    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encode;
    }

    private void mountainResponse() {

        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String uname = mPreferences.getString(getString(R.string.username_save), "");

        //Validate user in the database
        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        Toast.makeText(getApplicationContext(),"SUCCESS "+jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Error " +jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }

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
                String name = UUID.randomUUID().toString();

                if(imageChanged == true) {
                    mEditor.putString("com.hikinghelper.cody.hikinghelper.imagepath", "https://hikinghelper.000webhostapp.com/connect/images/" + name + ".png");
                    mEditor.commit();
                }

                String image = getStringImage(bitmap1);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("first_name", firstName.getText().toString());
                hashMap.put("age", age.getText().toString());
                hashMap.put("experience", experience.getText().toString());
                hashMap.put("about_me", aboutMe.getText().toString());
                hashMap.put("username", uname);
                hashMap.put("image_name", name);
                hashMap.put("image_path", image);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
}
