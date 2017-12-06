package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private EditText username, password;
    private Button sign_in_register;
    private CheckBox remember;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/user_login.php";
    private StringRequest request;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        sign_in_register = (Button) findViewById(R.id.btnLoginUser);
        remember = (CheckBox) findViewById(R.id.checkBox);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mEditor.remove(getString(R.string.first_name));
        mEditor.commit();
        mEditor.remove(getString(R.string.age));
        mEditor.commit();
        mEditor.remove(getString(R.string.experience));
        mEditor.commit();
        mEditor.remove(getString(R.string.about_me));
        mEditor.commit();
        mEditor.remove(getString(R.string.imagepath));
        mEditor.commit();

        checkSharedPreferences();

        requestQueue = Volley.newRequestQueue(this);

        sign_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if any fields are left blank
                if (isEmptyField(username)) return;
                else if (isEmptyField(password)) return;
                else{

                    //Validate user in the database
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                //Get JSON Array userInfo
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("userInfo");
                                JSONObject data = jsonArray.getJSONObject(0);

                                //Save or don't save credentials based off of checkbox
                                    if(remember.isChecked()){
                                        mEditor.putString(getString(R.string.checkbox), "True");
                                        mEditor.commit();

                                        //Save username
                                        String un = username.getText().toString();
                                        mEditor.putString(getString(R.string.username), un);
                                        mEditor.commit();

                                        //Save password
                                        String ps = password.getText().toString();
                                        mEditor.putString(getString(R.string.password), ps);
                                        mEditor.commit();
                                    }else{
                                        mEditor.putString(getString(R.string.checkbox), "False");
                                        mEditor.commit();

                                        //Save empty string
                                        mEditor.putString(getString(R.string.username), "");
                                        mEditor.commit();

                                        //Save empty string
                                        mEditor.putString(getString(R.string.password), "");
                                        mEditor.commit();
                                    }

                                   //Save username
                                      String un = data.getString("username");
                                      mEditor.putString(getString(R.string.username_save), un);
                                      mEditor.commit();

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

                                      //Save image path
                                      String ip = data.getString("image_path");
                                      mEditor.putString("com.hikinghelper.cody.hikinghelper.imagepath", ip);
                                      mEditor.commit();

                                    Toast.makeText(getApplicationContext(), "Success! Welcome to Hiking Helper!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Home.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Sorry. Invalid Login Credentials.", Toast.LENGTH_SHORT).show();
                            }}
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("username", username.getText().toString());
                            hashMap.put("password", password.getText().toString());
                            return hashMap;
                        }
                    };

                    requestQueue.add(request);
                }
            }
        });

        //set action bar text
        getSupportActionBar().setTitle("Login");

        Button btnNewUser=(Button)findViewById(R.id.btnNew);

        //Send user to Login page
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, create_new_user.class));
            }
        });
    }

    private void checkSharedPreferences() {
        String checkbox = mPreferences.getString(getString(R.string.checkbox), "False");
        String name = mPreferences.getString(getString(R.string.username), "");
        String pass = mPreferences.getString(getString(R.string.password), "");

        username.setText(name);
        password.setText(pass);

        if(checkbox.equals("True")) {
            remember.setChecked(true);
        }else{
            remember.setChecked(false);
        }
    }

    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getApplicationContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
        return result;
    }
}
