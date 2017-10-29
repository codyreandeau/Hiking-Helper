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

import java.util.HashMap;
import java.util.Map;

public class create_new_user extends AppCompatActivity {

    private EditText username, password, email, first_name, last_name, age;
    private Button register;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/user_register.php";
    private StringRequest request;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        email = (EditText) findViewById(R.id.txtEmail);
        first_name = (EditText) findViewById(R.id.txtFirstName);
        last_name = (EditText) findViewById(R.id.txtLastName);
        age = (EditText) findViewById(R.id.txtAge);
        register = (Button) findViewById(R.id.btnCreate);

        requestQueue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmptyField(username)) return;
                else if (isEmptyField(password)) return;
                else if (isEmptyField(email)) return;
                else if (isEmptyField(first_name)) return;
                else if (isEmptyField(last_name)) return;
                else if (isEmptyField(age)) return;
                else {

                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                    String first_name_value = first_name.getText().toString();
                                    String age_value = age.getText().toString();
                                    Intent intent = new Intent(getApplicationContext(), User.class);
                                    intent.putExtra("FIRST_NAME", first_name_value);
                                    intent.putExtra("AGE", age_value);
                                    startActivityForResult(intent, REQUEST_CODE);

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
                            hashMap.put("email", email.getText().toString());
                            hashMap.put("first_name", first_name.getText().toString());
                            hashMap.put("last_name", last_name.getText().toString());
                            hashMap.put("age", age.getText().toString());

                            return hashMap;
                        }
                    };

                    requestQueue.add(request);
                }
            }
        });

        //set action bar text
        getSupportActionBar().setTitle("Create New User");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(create_new_user.this, login.class));
            }
        });
    }


    private boolean isEmptyField (EditText editText){
        boolean result = editText.getText().toString().length() <= 0;
        if (result)
            Toast.makeText(getApplicationContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
        return result;
    }


}
