package com.hikinghelper.cody.hikinghelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class createEvent extends AppCompatActivity {

    EditText name, location, date, time, description;
    Button create;
    private RequestQueue requestQueue;
    private static final String URL = "https://hikinghelper.000webhostapp.com/connect/create_event.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.txtName);
        location = (EditText) findViewById(R.id.txtLocation);
        date = (EditText) findViewById(R.id.txtDate);
        time = (EditText) findViewById(R.id.txtTime);
        description = (EditText) findViewById(R.id.txtDescrption);
        create = (Button) findViewById(R.id.btnCreate);
        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String uname = mPreferences.getString(getString(R.string.username_save), "");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if any of the text fields are empty
                if (isEmptyField(name)) return;
                else if (isEmptyField(location)) return;
                else if (isEmptyField(date)) return;
                else if (isEmptyField(time)) return;
                else if (isEmptyField(description)) return;
                else if (!date.getText().toString().matches("\\d{4}-\\d{2}-\\d{2}"))
                    Toast.makeText(getApplicationContext(), "Date format should be YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                else {

                    //Validate user in the database
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("success") + " Event Created!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(createEvent.this, event.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                            hashMap.put("name", name.getText().toString());
                            hashMap.put("location", location.getText().toString());
                            hashMap.put("date", date.getText().toString());
                            hashMap.put("time", time.getText().toString());
                            hashMap.put("description", description.getText().toString());
                            hashMap.put("created_by", uname);
                            return hashMap;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        });

        //set action bar text
        getSupportActionBar().setTitle("Create an Event");

        //send user back to the event page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(createEvent.this, event.class));
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
