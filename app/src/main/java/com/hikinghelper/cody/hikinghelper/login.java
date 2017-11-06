package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
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
        //mPreferences = getSharedPreferences("com.hikinghelper.cody.hikinghelper", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

        requestQueue = Volley.newRequestQueue(this);

        sign_in_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if any fields are left blank
                String strUserName = username.getText().toString();
                String strPassword = password.getText().toString();
                if (strPassword.trim().equals("") && strUserName.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill out both fields", Toast.LENGTH_SHORT).show();
                }else if(strUserName.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill out the username field", Toast.LENGTH_SHORT).show();
                }else if(strPassword.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill out the password field", Toast.LENGTH_SHORT).show();
                } else {

                    //Validate user in the database
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                    if(remember.isChecked()){
                                        //Set checkbox on start up
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
                                        //Set checkbox on start up
                                        mEditor.putString(getString(R.string.checkbox), "False");
                                        mEditor.commit();

                                        //Save username
                                        mEditor.putString(getString(R.string.username), "");
                                        mEditor.commit();

                                        //Save password
                                        mEditor.putString(getString(R.string.password), "");
                                        mEditor.commit();
                                    }
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // System.out.println(error.getMessage());
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
}
